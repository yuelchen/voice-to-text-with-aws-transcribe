package com.yuelchen.transcribe;

import java.util.ArrayList;
import java.util.List;

public class Segment {
	
	private String speakerLabel;
	
	private String startTime;
	
	private String endTime;
	
	private List<Item> items;
	
	public Segment(String speakerLabel, String startTime, String endTime) {
		this.speakerLabel = speakerLabel;
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.items = new ArrayList<>();
	}
	
	public void addItem(String startTime, String endTime) {
		Item item = new Item(startTime, endTime);
		this.items.add(item);
	}
	
	public String getSpeakerLabel() {
		return this.speakerLabel;
	}
	
	public String getStartTime() {
		return this.startTime;
	}
	
	public String getEndTime() {
		return this.endTime;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public List<Item> getItems() {
		return this.items;
	}
	
	public String getSegmentContent() {
		StringBuilder sb = new StringBuilder();
		for(Item item: this.items) {
			sb.append(item.getContent() + " ");
		}
		
		return sb.toString();
	}
}
