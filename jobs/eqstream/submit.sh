#!/bin/bash

chmod +x wait-for-it.sh
./wait-for-it.sh jobmanager:8081 -t 0
./wait-for-it.sh kafka:9092 -t 0

echo "Submitting jar file for Earthquake Streaming Job..."

response=$(curl -X POST -H "Expect:" -F "jarfile=@target/eqstream-0.0.1-SNAPSHOT.jar" http://jobmanager:8081/jars/upload)
echo "response: $response"

filename=$(echo "$response" | grep -oP '"filename":"\K[^"]+')
id=$(echo "$filename" | awk -F'/' '{print $NF}')
echo "Extracted ID: $id"

curl -X POST http://jobmanager:8081/jars/"$id"/run
