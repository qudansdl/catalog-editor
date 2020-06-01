<template>
  <div class="q-pa-md">
    <q-card>
      <div class="row q-col-gutter-xs">
        <div class="col">
          <q-card-section>
            <q-input v-model="url" filled type="url" label="이미지 URL"/>
          </q-card-section>
        </div>
        <div class="col-auto">
          <q-card-section>
            <q-btn @click="addImage()">이미지 추가</q-btn>
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
