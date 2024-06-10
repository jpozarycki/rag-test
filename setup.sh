#!/bin/bash

echo "Starting containers..."
docker compose up -d

if [ $? -eq 0 ]; then
    echo "Ollama container started successfully."
    echo "Running llama3:8b in the container..."
    docker exec -d ollama ollama run llama3:8b
    echo "Please wait..."
    if [ $? -eq 0 ]; then
        echo "Ollama with llama3:8b is running successfully."
    else
        echo "Failed to pull llama3:8b."
    fi
else
    echo "Failed to start the containers."
fi