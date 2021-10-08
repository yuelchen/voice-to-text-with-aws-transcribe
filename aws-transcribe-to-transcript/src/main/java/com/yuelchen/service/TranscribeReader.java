package com.yuelchen.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yuelchen.transcribe.Item;
import com.yuelchen.transcribe.Segment;

public class TranscribeReader {
	
	private int speakerCount = 0;
	
	private final String inputFile;
	
	public TranscribeReader(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public List<Segment> getTranscriptList() throws JSONException {
			JSONObject resultsObject = this.getTranscribeResults();
			
			this.setSpeakerCount(resultsObject);
			List<Segment> segments = this.getSpeakerSegments(resultsObject);
			List<Item> items = this.getSpeakerSegmentItems(resultsObject);
			
			return generateTranscriptionList(segments, items);
	}
	
	private String getFileContent() {
		StringBuilder sb = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(this.inputFile))) {
			stream.forEach(line -> {
	        	sb.append(line);
	        });
		} catch(IOException e) {
			System.out.println(e.getMessage() + ", caused by " + e.getCause());
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	private JSONObject getTranscribeResults() throws JSONException {
		JSONObject jsonObject = new JSONObject(this.getFileContent());
		return jsonObject.getJSONObject("results");
	}
	
	private void setSpeakerCount(JSONObject resultsObject) {
		this.speakerCount = resultsObject.getJSONObject("speaker_labels").has("speakers") ? 
				resultsObject.getJSONObject("speaker_labels").getInt("speakers") : 0;
	}
	
	public List<Segment> getSpeakerSegments(JSONObject resultsObject) {
		List<Segment> segments = new ArrayList<>();
		
		JSONArray segmentsJSONArray = resultsObject
				.getJSONObject("speaker_labels")
				.getJSONArray("segments");
		
		List<JSONObject> segmentsObjects = IntStream
				.range(0, segmentsJSONArray.length())
				.mapToObj(index -> segmentsJSONArray.getJSONObject(index))
				.collect(Collectors.toList());
		
		
		segmentsObjects.stream().forEach(segmentObject -> {
			JSONObject segmentJSONObject = (JSONObject) segmentObject;
				Segment segment = new Segment(segmentJSONObject.getString("speaker_label"), 
						segmentJSONObject.getString("start_time"), 
						segmentJSONObject.getString("end_time"));
			
				JSONArray segmentItemsArray = segmentJSONObject.getJSONArray("items");
				List<JSONObject> segmentItemsObjects = IntStream
						.range(0, segmentItemsArray.length())
						.mapToObj(index -> segmentItemsArray.getJSONObject(index))
						.collect(Collectors.toList());
				
				segmentItemsObjects.stream().forEach(segmentItemObject -> {
					JSONObject segmentItemJSONObject = (JSONObject) segmentItemObject;
					segment.addItem(segmentItemJSONObject.getString("start_time"), segmentItemJSONObject.getString("end_time"));
				});
				
				segments.add(segment);
		});
		
		return segments;
	}
	
	public List<Item> getSpeakerSegmentItems(JSONObject resultsObject) {
		List<Item> items = new ArrayList<>();
		
		JSONArray itemsJSONArray = resultsObject
				.getJSONArray("items");
		
		List<JSONObject> itemsObjects = IntStream
				.range(0, itemsJSONArray.length())
				.mapToObj(index -> itemsJSONArray.getJSONObject(index))
				.collect(Collectors.toList());
		
		itemsObjects.stream().forEach(itemObject -> {
			JSONObject itemJSONObject = (JSONObject) itemObject;
			
			if(itemJSONObject.has("start_time")) {
				Item item = new Item(itemJSONObject.getString("start_time"), 
						itemJSONObject.getString("end_time"));
				
				JSONArray alternativesArray = itemJSONObject.getJSONArray("alternatives");
				List<JSONObject> alternativesObjects = IntStream
						.range(0, alternativesArray.length())
						.mapToObj(index -> alternativesArray.getJSONObject(index))
						.collect(Collectors.toList());
				
				alternativesObjects.stream().forEach(alternativeObject -> {
					JSONObject alternativeJSONObject = (JSONObject) alternativeObject;
					item.addItemContent(alternativeJSONObject.getString("confidence"), alternativeJSONObject.getString("content"));
				});

				items.add(item);
			} else {
				String punctuation = itemJSONObject
						.getJSONArray("alternatives")
						.getJSONObject(0)
						.getString("content");
				
				int lastItemIndex = items.size() - 1;
				String currentContent = items.get(lastItemIndex).getContent();
				items.get(lastItemIndex).setContent(currentContent + punctuation);
			}
		});
		
		return items;
	}
	
	public List<Segment> generateTranscriptionList(List<Segment> segments, List<Item> items) {
		
		int itemsCounter = 0;
		for(Segment segment : segments) {
			List<Item> segmentItems = segment.getItems();
			for(Item segmentItem : segmentItems) {
				Item item = items.get(itemsCounter);
				
				if(segmentItem.compareTo(item) == 1) {
					segmentItem.setConfidence(item.getConfidence());
					segmentItem.setContent(item.getContent());
				} else {
					segmentItem.setContent("N/A");
				}
				
				itemsCounter++;
			}
			
			segment.setItems(segmentItems);
		}
		
		return segments;
	}
	
	public int getSpeakerCount() {
		return this.speakerCount;
	}
}