# RAG Test
## Description
This is a simple test RAG application running on Java with Spring, Qdrant Vector Database, Redis for simple document management and llama3 as underlying LLM.

## Requirements
- Java 17
- Docker

## Running the application

* Run the `./setup.sh` to pull images, run QDrant, Redis and Ollama containers. After that, llama3:8b will be running inside of Ollama container.


* Configure the application in your favourite IDE:
 - add environment variable `JINA_API_KEY` with a key from [Jina.AI](https://jina.ai/#apiform)
* Run the `RagTestApplication` class.

## Upload API

### POST /upload

Uploads a document to the vector store.

#### Request

| Parameter | Type | Description |
|-----------|------|-------------|
| document | MultipartFile | The document to be uploaded. |

#### Response

| Parameter | Type | Description |
|-----------|------|-------------|
| status | Enum (UploadStatus) | The status of the upload operation. Can be `SUCCESS` or `ERROR`. |
| documentId | String | The ID of the uploaded document. This is null if the upload operation was not successful. |
| errorMessage | String | The error message if the upload operation was not successful. This is null if the upload operation was successful. |

#### Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 | The document was successfully uploaded. |
| 400 | The document could not be uploaded due to an error or because the document was null or empty. |


## Question API

### POST /question

Submits a question to the system.

#### Request

| Parameter | Type | Description |
|-----------|------|-------------|
| questionText | String | The text of the question. |

#### Response

| Parameter | Type | Description |
|-----------|------|-------------|
| status | Enum (QuestionStatus) | The status of the question submission. Can be `SUCCESS` or `ERROR`. |
| questionId | String | The ID of the submitted question. This is null if the submission was not successful. |
| errorMessage | String | The error message if the question submission was not successful. This is null if the submission was successful. |

#### Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 | The question was successfully submitted. |
| 400 | The question could not be submitted due to an error or because the question text was null or empty. |
