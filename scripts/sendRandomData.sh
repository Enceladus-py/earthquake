#!/bin/bash

echo "--- Script for sending RANDOM Latitude, Longitude and Magnitude data for an earthquake ---"

echo "Start sending..."

while true; do
    lat=$(awk -v seed="$RANDOM" 'BEGIN{srand(seed); print -90+180*rand()}')
    lon=$(awk -v seed="$RANDOM" 'BEGIN{srand(seed); print -180+360*rand()}')
    mag=$(awk -v seed="$RANDOM" 'BEGIN{srand(seed); printf "%.1f\n", 1+9*rand()}')

    curl -X POST http://localhost:8080/earthquakes -d "{\"lat\": $lat, \"lon\": $lon, \"magnitude\": $mag}" -H "Content-Type: application/json"

    printf "\n"

    sleep 1
done
