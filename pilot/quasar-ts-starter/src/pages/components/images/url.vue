<template>
    <div>
        <div class="img-url">
          <q-input outlined v-model="url" placeholder="Image Url" />

          <q-btn dense flat icon="add" @click="addImage">
            <q-tooltip content-class="bg-white text-primary">추가</q-tooltip>
          </q-btn>
        </div>
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
import imageToDataUri from '@/utils/image-to-data-uri';

export default {
  data() {
    return {
      url: '',
      images: [],
      selected: null,
    };
  },
  methods: {
    async setSelected(img) {
      this.selected = img;
      imageToDataUri(img, (err, uri) => {
        this.$emit('imageSelected', uri);
      });
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
        this.images.push(this.url);
        this.url = '';
      }
    },
    deleteBtn(item) {
      this.images.splice(item, 1);
    },
    validURL() {
      return (this.url.match(/\.(jpeg|jpg|gif|png)$/) != null);
    },
  },
  computed: {

  },
};
</script>

<style>
  .img-url {
    display: flex;
  }
  img{
    padding: 2px;
    z-index: 10000;
    width: 250px;
    height: 200px;
  }
  button {
    background: none;
    border: none;
    cursor: pointer;
    margin: auto;
    display: block;
  }
  .image {
    float: left;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
    border: 1px solid #ebebeb;
    margin: 5px;
  }
  .vue-select-image__item {
    margin-left: 0px !important;
  }
</style>
