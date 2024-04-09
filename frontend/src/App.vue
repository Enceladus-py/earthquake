<script setup lang="ts">
import { ref, provide, type Ref } from 'vue'
import {GoogleMap, Marker, InfoWindow} from "vue3-google-map";
import LatLonScript from "@/components/LatLonScript.vue";

export interface MarkerData {
  position: {
    lat: number;
    lng: number;
  };
  magnitude: number;
  id: number;
}

const center = { lat: 38.9637, lng: 35.2433 }
const markers : Ref<MarkerData[]> | undefined  = ref([])

provide('markers', markers)

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
          <h1 id="firstHeading" class="firstHeading">Magnitude</h1>
          <div id="bodyContent">
            <p>{{marker.magnitude}}</p>
          </div>
        </div>
      </InfoWindow>
    </Marker>
  </GoogleMap>
  <LatLonScript></LatLonScript>
</template>

