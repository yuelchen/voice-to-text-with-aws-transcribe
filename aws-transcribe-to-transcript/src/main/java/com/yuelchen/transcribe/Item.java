package com.yuelchen.transcribe;

public class Item implements Comparable<Item>{
	
	private String startTime;
	
	private String endTime;
	
	private double confidence;
	
	private String content;
	
	public Item(String startTime, String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.confidence = 0.0;
		this.content = "";
	}
	
	public void addItemContent(String confidence, String content) {
		double contentConfidence = Double.parseDouble(confidence);
		if(this.confidence < contentConfidence) {
			this.confidence = contentConfidence;
			this.content = content;
		}
	}
	
	public String getStartTime() {
		return this.startTime;
	}
	
	public String getEndTime() {
		return this.endTime;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	public double getConfidence() {
		return this.confidence;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	@Override
	public int compareTo(Item item) {
		return (this.startTime.equals(item.getStartTime()) && this.endTime.equals(item.endTime)) ? 1 : 0;
	}
}