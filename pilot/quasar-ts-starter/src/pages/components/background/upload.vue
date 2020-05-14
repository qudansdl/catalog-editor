<template>
  <div class="p-2">
    <q-file
      v-model="file"
      label="파일 선택"
      filled
      style="max-width: 300px"
    />
    <q-btn @click="uploadFile()">업로드</q-btn>
    <div class="row p-0 m-0">
      <div class=" col-6 p-0 m-0" v-for="(item, k) of images" :key="k" >
        <q-btn :color="item === selected ? 'primary' : ''" @click="setSelected(item)">
          <q-img
            :src="item"
            spinner-color="white"
            style="height: 250px; width: 220px"
          />
        </q-btn>
        <q-btn dense flat icon="delete" @click="deleteBtn(k)">
          <q-tooltip content-class="bg-white text-primary">삭제</q-tooltip>
        </q-btn>
      </div>
    </div>
  </div>
</template>

<script>
import ApiImage from '@/api/images';

export default {
  data() {
    return {
      images: [],
      file: null,
      selected: null,
    };
  },
  methods: {
    async uploadFile() {
      const { data } = await ApiImage.uploadImages(this.file);

      const { content } = data.createImage;
      this.images.push(content);
      this.file = null;
    },

    setSelected(img) {
      this.selected = img;
      this.$emit('imageSelected', img);
    },
    deleteBtn(item) {
      this.images.splice(item, 1);
    },
  },
};
</script>

<style>
  .img-btn {
    cursor: pointer;
  }
  .delete-btn{
    position: absolute;
    width: 100%;
    right: 0;
    top: 0;
    color: transparent;
    font-size: 20px;
  }
  .delete-btn:hover {
    background: rgba(0, 0, 0, 0.329);
    color: white;
  }
</style>
