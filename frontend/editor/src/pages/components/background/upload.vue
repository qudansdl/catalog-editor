<template>
  <div class="q-pa-md">
    <q-card>
      <div class="row q-col-gutter-xs">
        <div class="col">
          <q-card-section>
            <q-file
              v-model="file"
              label="파일 선택"
              filled
            />
          </q-card-section>
        </div>
        <div class="col-auto">
          <q-card-section>
            <q-btn @click="uploadFile()">업로드</q-btn>
          </q-card-section>
        </div>
      </div>
      <div class="row q-col-gutter-xs">
          <q-card-section>
            <vue-select-image
              :dataImages="images"
              :w="'250px'"
              :h="'200px'"
              @onselectimage="onSelectImage"/>
          </q-card-section>
      </div>
    </q-card>
  </div>
</template>

<script>
import VueSelectImage from '@/components/VueSelectImage/VueSelectImage.vue';

import { v4 as uuidv4 } from 'uuid';

import imageToDataUri from '@/utils/image-to-data-uri';
import ApiImage from '@/api/images';

export default {
  data() {
    return {
      images: [],
      file: null,
      selected: null,
    };
  },
  components: {
    VueSelectImage,
  },
  methods: {
    async uploadFile() {
      const { data } = await ApiImage.uploadImages(this.file);

      const { content } = data.createImage;

      const img = { id: uuidv4().toUpperCase(), src: content };
      this.images.push(img);
      this.onSelectImage(img);
      this.file = null;
    },

    onSelectImage(img) {
      this.selected = img;
      this.$emit('imageSelected', img.src);
    },
  },
};
</script>
