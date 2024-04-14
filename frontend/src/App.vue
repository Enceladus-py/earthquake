<script setup lang="ts">
import { ref, provide, type Ref } from 'vue'
import {GoogleMap, Marker, InfoWindow} from "vue3-google-map";
import LatLonScript from "@/components/LatLonScript.vue";
import axios from "axios";

export interface MarkerData {
  position: {
    lat: number;
    lng: number;
  };
  magnitude: number;
  id: number;
}

const markers : Ref<MarkerData[]> | undefined  = ref([])
const count = ref(0)

axios.get('http://localhost:8080/earthquakes/list')
    .then(response => {
      response.data.forEach( (eq:any) => {
        markers.value.push({
          position: {
            lat: eq.lat,
            lng: eq.lon
          },
          magnitude: eq.magnitude,
          id: eq.id,
        }
      )
        count.value += 1
      }
    )})
    .catch(error => {
      console.error(error)
    })

const center = { lat: 38.9637, lng: 35.2433 }

provide('markers', markers)
provide('count', count)

</script>

<template>
  <h3>Earthquake App</h3>
  <GoogleMap
      api-key="AIzaSyAi3Vw0HIEUoFiphC9jLjm757NpgL946tA"
      style="width: 100%; height: 500px"
      :center="center"
      :zoom="5"
  >
    <Marker
        v-for="(marker, index) in markers"
        :key="index"
        :options="{position:marker.position, clickable: true}"
    >
      <InfoWindow>
        <div id="content">
          <div id="siteNotice"></div>
          <h1 id="firstHeading" class="firstHeading">Information</h1>
          <div id="bodyContent">
            <p>Magnitude: {{marker.magnitude}}</p>
            <p>Position: {{marker.position}}</p>
          </div>
        </div>
      </InfoWindow>
    </Marker>
  </GoogleMap>
  <LatLonScript></LatLonScript>
</template>

