# aws-transcribe-transcript
A Java project which parses the JSON file content from AWS Transcribe into a readable transcript. 

### About
Ever record a long meeting or converstation but you can't find the information you're looking for?
Want an easier way to visually confirm a voice / conversation recording rather spending time skimming or listening to an audio recording from end-to-end?
AWS Transcribe translates your recordings to text with little cost and pay-for-what-you-use model. 
The output however is a JSON file and their console transcript only displays so much (about the first 1000 words). 
This works great for those who have very short recordings but not so great if your output is huge or needs to be automated or scaled. 
Now you can use this Java project to easily parse large JSON outputs into transcripts in a fairly quick manner and similar to same output format as AWS Transcribe. 

### Performance Metrics
Input File Size: 5.11 MB
Output File Size: 133 KB
- 1st run: 2311 milliseconds
- 2nd run: 2344 milliseconds
- 3rd run: 789 milliseconds
- 4th run: 607 milliseconds
- 5th run: 600 milliseconds

### Pre-requisite
- Install Java (version 8+) on your environment; see shake-out example output below.
```:
C:\> java --version
java 13.0.2 2020-01-14
Java(TM) SE Runtime Environment (build 13.0.2+8)
Java HotSpot(TM) 64-Bit Server VM (build 13.0.2+8, mixed mode, sharing)
```

### How-to Guide
1. Get the jar file onto your environment. 
2. Once the jar file is in place, you can trigger it using the below command as a template.
```:
C:\> java -jar aws-transcribe-to-transcript-0.0.1.jar input.json output.txt
```
  - The input.json file is the AWS Transcribe generated file. 
  - The output.txt is the filename for the transcript. 

### Sample Input JSON Content:
```JSON:
{
  jobName:"aws-transcribe-job-1",
  accountId:"123456789123",
  results: {
    transcripts: [{
      transcript: "hello hi there..."
    }],
    speaker_labels: {
      speakers: 2,
      segments: [{
        start_time: "0.04",
        speaker_label: "spk_1",
        end_time: "1.5"
      }, {
        start_time: "1.5",
        speaker_label: "spk_0",
        end_time: "5.61"
      },...]
    },
    items: [{
      start_time: "0.39",
      end_time: "1.5",
      alternatives: [{
        confidence: "0.986",
        content: "hello"
      }],
      type: "pronunciation"
    }, {
      start_time: "1.5",
      end_time: "1.68",
      alternatives: [{
        confidence: "1.0",
        content: "hi"
      }],
      type: "pronunciation"
    },...]
  },
  status: "COMPLETED"
}
```

### Sample Output Text File Content
```:
spk_1: hello
spk_0: hi there...
...
```
