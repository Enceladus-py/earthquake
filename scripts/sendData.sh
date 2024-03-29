#!/bin/bash

echo "--- Script for sending Latitude, Longitude and Magnitude data for an earthquake ---"

read -p "Enter latitude : " lat

read -p "Enter your longitude : " lon

read -p "Enter your magnitude : " mag

echo -n "Data will be sent: $lat, $lon, $mag. OK? (y/n) "

read choice

if [ "$choice" != "y" ]; then
    echo "Data transmission cancelled."
    exit 0
fi

echo "Data transmission in progress..."

echo -e "Response:"

curl -X POST http://localhost:8080/earthquakes -d '{"lat": "'"$lat"'", "lon": "'"$lon"'", "magnitude": "'"$mag"'"}' -H 'Content-Type: application/json'

printf "\n"