<template>
    <div>
        <div v-for="n in images" :key="n">
            <button @click="addForm(n)">
                <img :src="n" alt="">
            </button>
        </div>
    </div>
</template>

<script>
import img1 from '@/assets/img/download.jpg';
import img2 from '@/assets/img/images.jpg';
import img3 from '@/assets/img/1.jpg';

export default {
  data() {
    return {
      images: [img1, img2, img3],
    };
  },
  methods: {
    addForm(img) {
      this.$emit('addText', img, 'img');
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
