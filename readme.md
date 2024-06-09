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