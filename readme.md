Watermark-Service
=================

This service creates watermarks for existing documents. Watermarks are created asynchronously. The Api offers means to
trigger a job, poll a job's state and fetch a document. A document can only be retrieved as soon as the watermarking is finished for it.

As this is a demo service there are some documents hard wired into the application. Use ids 1,2,3,... to play around with them,
A watermarking job will take 30 to 60 seconds in this demo.

## Prerequisites

- git
- java 8
- maven 3


## Building and running tests

- `git clone <>` checks out the project from github
- `mvn clean package` builds the artifact
- `java -jar target/watermark-service-1.0-SNAPSHOT.jar` runs the artifact
- `mvn test` runs unit tests
- `mvn test todo` runs integration tests


## REST API

- The endpoint for creating and polling state of watermark jobs is 

        http://<server>:<port>/wm/watermarkjobs
        
                
- The endpoint for fetching documents is 

        http://<server>:<port>/wm/documents

- Curl examples for
    - creating a job

        curl -H "Accept: application/json"  -H "Content-Type: application/json" -d '{ "documentId" : 1 }' -X POST http://localhost:8080/wm/watermarkjobs
         
        response: {"ticket":"fc557f83-ff5a-4642-8b2e-48450cbd96f6"}

    - fetching job state using the previously received ticket as ***url parameter***

        curl -H "Accept: application/json"  -H "Content-Type: application/json" -X GET http://localhost:8080/wm/watermarkjobs/fc557f83-ff5a-4642-8b2e-48450cbd96f6
        
        response: {"finished":"true"} or {"finished":"false"}

    - fetching a document with the ticket provided as header ***x-wm-ticket***
        
        curl -H "Accept: application/json"  -H "Content-Type: application/json" -H "x-wm-ticket: fc557f83-ff5a-4642-8b2e-48450cbd96f6" -X GET http://localhost:8080/wm/documents/1
        
        response: 

        // Optional here is not yet supported by jackson to properly transform it to json