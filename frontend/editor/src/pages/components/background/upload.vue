<template>
  <q-card>
    <q-card-section class="row justify-center">
      <q-file
        v-model="file"
        label="파일 선택"
        filled
        style="max-width: 300px"
      >
        <template v-slot:after v-if="canUpload">
          <q-btn
            color="primary"
            dense
            icon="cloud_upload"
            round
            @click="uploadFile"
            :disable="!canUpload"
          />
        </template>
      </q-file>
    </q-card-section>
    <q-card-section class="row justify-center">

      <vue-select-image
        :dataImages="images"
        :w="'250px'"
        :h="'200px'"
        @onselectimage="onSelectImage"/>
    </q-card-section>
  </q-card>
</template>


<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

import VueSelectImage from '@/components/VueSelectImage/VueSelectImage.vue';
import imageToDataUri from '@/utils/image-to-data-uri';
import { v4 as uuidv4 } from 'uuid';
import { IBackgroundData } from '@/api/types';

@Component({
  components: {
    VueSelectImage
  }
})
export default class UploadImage extends Vue {
  private images: IBackgroundData[] = []
  private file: any = null
  private selected: IBackgroundData | null = null

  get canUpload() {
    return this.file !== null
  }

  uploadFile() {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      const dataURI = e.target.result;
      if (dataURI) {
        const img = { id: uuidv4().toUpperCase(), content: dataURI } as IBackgroundData;
        this.images.push(img);
        this.onSelectImage(img);
        this.file = null;
      }
    };

    // read blob url from file data
    reader.readAsDataURL(this.file);
  }

  onSelectImage(img: IBackgroundData) {
    this.selected = img;
    this.$emit('imageSelected', img.content);
  }
};
</script>
