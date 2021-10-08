package com.yuelchen.main;

import java.util.List;

import com.yuelchen.service.TranscribeReader;
import com.yuelchen.service.TranscriptWriter;
import com.yuelchen.transcribe.Segment;

public class Application {
	
	public static void main(String[] args) {
		
		if(args.length == 2) {
			long runtimeStart = System.currentTimeMillis();
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			TranscribeReader tr = new TranscribeReader(inputFile);
			List<Segment> segments = tr.getTranscriptList();
			
			System.out.println(String.format("There are %d speakers in this transcript", tr.getSpeakerCount()));
			segments.stream().forEach(segment -> {
				System.out.println(String.format("%s (%s - %s): %s", 
						segment.getSpeakerLabel(),
						segment.getStartTime(),
						segment.getEndTime(),
						segment.getSegmentContent()));
			});
			
			int statusCode = TranscriptWriter.generateOuputTranscript(outputFile, segments);
			if(statusCode == 1) {
				System.out.println(String.format("Successfully written to output file %s", outputFile));
			} else if(statusCode == 0) {
				System.out.println("Attempted to write full transcript but failed, see output for more info.");
			} else {
				System.out.println("Hard failure, didn't even begin writing to file. See output for more info.");
			}

			long runtimeEnd = System.currentTimeMillis();
			System.out.println(String.format("Runtime: %d MS", (runtimeEnd - runtimeStart)));
			
		} else {
			System.out.println("Invalid number of arguments, requires input file (arg0) "
					+ "and output file (arg1) to be passed during invocation");
		}
	}
}