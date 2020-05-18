<template>
  <div class="layout-padding">
    <vue-select-image
      :dataImages="images"
      :w="'250px'"
      :h="'200px'"
      @onselectimage="onSelectImage"/>
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

import VueSelectImage from '@/components/VueSelectImage/VueSelectImage.vue';

import imageToDataUri from '@/utils/image-to-data-uri';

export default {
  data() {
    return {
      images: [
        { id: `${ptn1}1`, src: ptn1, alt: ptn1 },
        { id: `${ptn2}2`, src: ptn2, alt: ptn2 },
        { id: `${ptn3}3`, src: ptn3, alt: ptn3 },
        { id: `${ptn4}4`, src: ptn4, alt: ptn4 },
        { id: `${ptn5}5`, src: ptn5, alt: ptn5 },
        { id: `${ptn6}6`, src: ptn6, alt: ptn6 },
        { id: `${ptn7}7`, src: ptn7, alt: ptn7 },

        { id: `${ptn1}8`, src: ptn1, alt: ptn1 },
        { id: `${ptn2}9`, src: ptn2, alt: ptn2 },
        { id: `${ptn3}10`, src: ptn3, alt: ptn3 },
        { id: `${ptn4}11`, src: ptn4, alt: ptn4 },
        { id: `${ptn5}12`, src: ptn5, alt: ptn5 },
        { id: `${ptn6}13`, src: ptn6, alt: ptn6 },
        { id: `${ptn7}14`, src: ptn7, alt: ptn7 },

        { id: `${ptn1}15`, src: ptn1, alt: ptn1 },
        { id: `${ptn2}16`, src: ptn2, alt: ptn2 },
        { id: `${ptn3}17`, src: ptn3, alt: ptn3 },
        { id: `${ptn4}18`, src: ptn4, alt: ptn4 },
        { id: `${ptn5}19`, src: ptn5, alt: ptn5 },
        { id: `${ptn6}20`, src: ptn6, alt: ptn6 },
        { id: `${ptn7}21`, src: ptn7, alt: ptn7 },

        { id: `${ptn1}22`, src: ptn1, alt: ptn1 },
        { id: `${ptn2}23`, src: ptn2, alt: ptn2 },
        { id: `${ptn3}24`, src: ptn3, alt: ptn3 },
        { id: `${ptn4}25`, src: ptn4, alt: ptn4 },
        { id: `${ptn5}26`, src: ptn5, alt: ptn5 },
        { id: `${ptn6}27`, src: ptn6, alt: ptn6 },
        { id: `${ptn7}28`, src: ptn7, alt: ptn7 },

        { id: `${ptn1}29`, src: ptn1, alt: ptn1 },
        { id: `${ptn2}30`, src: ptn2, alt: ptn2 },
        { id: `${ptn3}31`, src: ptn3, alt: ptn3 },
        { id: `${ptn4}32`, src: ptn4, alt: ptn4 },
        { id: `${ptn5}33`, src: ptn5, alt: ptn5 },
        { id: `${ptn6}34`, src: ptn6, alt: ptn6 },
        { id: `${ptn7}34`, src: ptn7, alt: ptn7 },
      ],
      selected: null,
      index: null,
    };
  },
  components: {
    VueSelectImage,
  },
  methods: {
    async onSelectImage(img) {
      this.selected = img;
      imageToDataUri(img.src, (err, uri) => {
        this.$emit('imageSelected', uri);
      });
    },
  },
};
</script>
