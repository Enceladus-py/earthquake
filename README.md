# Earthquake App

This is the repo for the kartaca assignment. `Java Spring Boot` is used for backend, `Vue.js` is used for frontend.

## How2Run 

frontend and backend are implemented as one container since it is not a big application. You can run the project simply typing:
```
docker compose up
```
This command will build the images and run the container. You can access frontend from port `5173` and backend from `8080`.

## Using scripts
Make sure that you have given permission to files:
```
chmod +x scripts/sendData.sh
chmod +x scripts/sendRandomData.sh
```
Then you can run the scripts.
- `sendData.sh` takes `lat`, `lot`, `mag` as input and sends it to backend server. 
- `sendRandomData.sh` generates random `lat`, `lot`, `mag` values and sends it to backend server. 

