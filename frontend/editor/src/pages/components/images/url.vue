<template>
  <q-card>
    <q-card-section class="row justify-center">

      <q-input
        v-model="url"
        debounce="500"
        filled
        type="url"
        placeholder="이미지 URL"
        hint="이미지 URL"
      >
        <template v-slot:append>
          <q-btn @click="addImage()">이미지 추가</q-btn>
        </template>
      </q-input>
    </q-card-section>
    <q-card-section>
          <vue-select-image
            :dataImages="images"
            :w="'250px'"
            :h="'200px'"
            @onselectimage="onSelectImage"/>
    </q-card-section>
  </q-card>
</template>

<script>
import VueSelectImage from '@/components/VueSelectImage/VueSelectImage.vue';
import { v4 as uuidv4 } from 'uuid';

import imageToDataUri from '@/utils/image-to-data-uri';
import ApiImage from '@/api/images';

export default {
  data() {
    return {
      url: '',
      images: [],
      selected: null,
    };
  },
  methods: {
    onSelectImage(img) {
      this.selected = img;
      this.$emit('imageSelected', img.src);
    },
    addImage() {
      if (!this.url) {
        this.$q.dialog({
          title: '안내',
          message: 'URL을 입력하세요',
        });
      } else if (!this.validURL()) {
        this.$q.dialog({
          title: '안내',
          message: 'URL을 입력하세요',
        });
      } else {
        imageToDataUri(this.url, (err, uri) => {
          const img = { id: uuidv4().toUpperCase(), src: uri };
          this.images.push(img);
          this.setSelected(img);
          this.url = '';
        });
      }
    },
    validURL() {
      return (this.url.match(/\.(jpeg|jpg|gif|png)$/) != null);
    },
  },
};
</script>
