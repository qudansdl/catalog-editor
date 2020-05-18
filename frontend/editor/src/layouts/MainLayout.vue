<template>
  <keep-alive>
      <router-view />
  </keep-alive>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class MainLayout extends Vue {

}
</script>
