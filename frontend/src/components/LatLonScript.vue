<script setup lang="ts">
import { ref, inject, type Ref } from 'vue'
import axios from 'axios'
import type { MarkerData } from '../App.vue'

interface MarkerObjData {
  lat: number,
  lon: number,
  magnitude: number,
}

let isClicked = false
let interval = 0
const count = ref(0)
const markers : Ref<MarkerData[]> | undefined = inject('markers')
const latitude = ref(0)
const longitude = ref(0)
const magnitude = ref(0)

const handleContButtonClick = () => {
  const button = document.getElementById('submitBtn')
  if (isClicked && button) {
    button.classList.remove('clicked')
    button.innerText = "Start sending"
    if (interval) {
      clearInterval(interval)
    }
  } else if (button) {
    button.classList.add('clicked')
    button.innerText = "Stop sending"
    interval = setInterval(sendMessage, 500)
  }
  isClicked = !isClicked
}

const addMarker = (data : MarkerObjData) => {
  axios.post('http://localhost:8080/earthquakes/add', data)
      .then(response => {
        console.log(response.data);
        markers?.value.push({
          position: {
            lat: response.data.lat,
            lng: response.data.lon
          },
          magnitude: response.data.magnitude,
          id: response.data.id,
        })
        count.value += 1
      })
      .catch(error => {
        console.error(error)
      })
}

const random = (min: number, max: number) => Math.floor(Math.random() * (max - min)) + min

const sendMessage = () => {
  addMarker({
    lat: random(0, 42),
    lon: random(0, 45),
    magnitude: random(0, 10),
  })
}

const deleteMarkers = () => {
  axios.get('http://localhost:8080/earthquakes/deleted')
      .then(response => {
        if(markers && markers.value){
          markers.value = markers.value.filter(x => !response.data.includes(x.id))
          count.value = markers.value.length
        }
      })
      .catch(error => {
        console.error(error)
      })
}
setInterval(deleteMarkers, 3600000)

const handleButtonClick = () => {
  console.log(latitude.value, longitude.value, magnitude.value)
  addMarker({
    lat: latitude.value,
    lon: longitude.value,
    magnitude: magnitude.value,
  })
}

</script>

<template>

  <div class="form-container">

    <form class="form">
      <div class="form-group">
        <label>Latitude:</label>
        <input type="text" v-model="latitude">
      </div>
      <div class="form-group">
        <label>Longitude:</label>
        <input type="text" v-model="longitude">
      </div>
      <div class="form-group">
        <label>Magnitude:</label>
        <input type="text" v-model="magnitude">
      </div>
      <button type="button" @click="handleButtonClick">Submit</button>
    </form>

    <form class="form">
      <button id="submitBtn" type="button" @click="handleContButtonClick">Start sending</button>
      <p> Count: {{count}}</p>
    </form>

  </div>

</template>

<style scoped>
.form-container {
  display: flex;
  justify-content: space-between;
  padding-top: 5%;
}

.form {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px;
  margin-right: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  font-weight: bold;
}

input[type="text"] {
  width: 80%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 3px;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

</style>