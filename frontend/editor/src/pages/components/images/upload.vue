<template>
  <div class="q-pa-md">
      <div class="row q-col-gutter-xs">
        <div class="col">
            <q-file
              v-model="file"
              label="파일 선택"
              filled
            />
        </div>
        <div class="col-auto">
            <q-btn @click="uploadFile()">업로드</q-btn>
        </div>
      </div>
      <div class="row q-col-gutter-xs">
          <vue-select-image
            :dataImages="images"
            :w="'250px'"
            :h="'200px'"
            @onselectimage="onSelectImage"/>
      </div>
  </div>
</template>


<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

import VueSelectImage from "@/components/VueSelectImage/VueSelectImage.vue";
import imageToDataUri from "@/utils/image-to-data-uri";
import {v4 as uuidv4} from "uuid";
import { IImageData } from '@/api/types';

@Component({
  components: {
    VueSelectImage
  }
})
export default class UploadImage extends Vue {
  private images: IImageData[] = []
  private file: any = null
  private selected: IImageData | null = null


  uploadFile() {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      const dataURI = e.target.result;
      if (dataURI) {
        const img = { id: uuidv4().toUpperCase(), content: dataURI } as IImageData;
        this.images.push(img);
        this.onSelectImage(img);
        this.file = null;
      }
    };

    // read blob url from file data
    reader.readAsDataURL(this.file);
  }

  onSelectImage(img: IImageData) {
    this.selected = img;
    this.$emit('imageSelected', img.content);
  }
};
</script>
