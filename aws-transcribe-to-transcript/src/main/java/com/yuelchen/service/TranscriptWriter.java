package com.yuelchen.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.yuelchen.transcribe.Segment;

public class TranscriptWriter {

	public static int generateOuputTranscript(String outputFile, List<Segment> segments) {
		int statusCode = 1;
		
		try {
			FileWriter fileWriter = new FileWriter(new File(outputFile));
			
			String previousSpeaker = "";
			for(Segment segment : segments) {
				if(previousSpeaker.equals(segment.getSpeakerLabel())) {
					fileWriter.write(String.format("%s", 
							segment.getSegmentContent()));
				} else {
					previousSpeaker = segment.getSpeakerLabel();
					fileWriter.write(String.format("\n%s: %s ", 
							segment.getSpeakerLabel(),
							segment.getSegmentContent()));
				}
			}
			
			fileWriter.close();
			
		} catch(IOException e) {
			System.out.println(e.getMessage() + ", caused by " + e.getCause());
			e.printStackTrace();
			statusCode = -1;
		}
		
		return statusCode;
	}
}