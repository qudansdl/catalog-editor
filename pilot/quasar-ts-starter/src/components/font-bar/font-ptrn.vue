<template>
  <div>
    <div v-for="n in images" :key="n">
      <q-btn :color="n === selected ? 'primary' : ''" @click="setSelected(n)">
        <q-img
          :src="n"
          spinner-color="white"
          style="height: 250px; width: 220px"
        />
      </q-btn>
    </div>
  </div>
</template>

<script>
import ptn1 from '@/assets/img/pattern/pattern1.jpg';
import ptn2 from '@/assets/img/pattern/pattern2.jpg';
import ptn3 from '@/assets/img/pattern/pattern3.jpg';
import ptn4 from '@/assets/img/pattern/pattern4.png';
import ptn5 from '@/assets/img/pattern/1.jpg';
import ptn6 from '@/assets/img/pattern/2.jpg';
import ptn7 from '@/assets/img/pattern/images.jpg';

export default {
  data() {
    return {
      images: [
        ptn1, ptn2, ptn3, ptn4, ptn5, ptn6, ptn7,
      ],
      selected: null,
    };
  },
  methods: {
    setSelected(img) {
      this.selected = img;
      this.$emit('imageSelected', img);
    },
  },
};
</script>

<style>
  img{
    padding: 2px;
    width: 100%;
    z-index: 10000;

    height: auto
  }
  button {
    background: none;
    border: none;
    cursor: pointer;
    margin: auto;
    display: block;
  }
</style>
