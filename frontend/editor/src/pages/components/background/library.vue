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
import img1 from '@/assets/img/download.jpg';
import img2 from '@/assets/img/images.jpg';
import img3 from '@/assets/img/1.jpg';

import VueSelectImage from '@/components/VueSelectImage/VueSelectImage.vue';

import imageToDataUri from '@/utils/image-to-data-uri';

export default {
  data() {
    return {
      images: [
        { id: `${img1}1`, src: img1, alt: img1 },
        { id: `${img2}2`, src: img2, alt: img2 },
        { id: `${img3}3`, src: img3, alt: img3 },

        { id: `${img1}4`, src: img1, alt: img1 },
        { id: `${img2}5`, src: img2, alt: img2 },
        { id: `${img3}6`, src: img3, alt: img3 },

        { id: `${img1}7`, src: img1, alt: img1 },
        { id: `${img2}8`, src: img2, alt: img2 },
        { id: `${img3}9`, src: img3, alt: img3 },

        { id: `${img1}10`, src: img1, alt: img1 },
        { id: `${img2}11`, src: img2, alt: img2 },
        { id: `${img3}12`, src: img3, alt: img3 },

        { id: `${img1}13`, src: img1, alt: img1 },
        { id: `${img2}14`, src: img2, alt: img2 },
        { id: `${img3}15`, src: img3, alt: img3 },
      ],
      selected: null,
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
