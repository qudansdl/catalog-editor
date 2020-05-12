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
export default {
  data() {
    return {
      url: '',
      images: [],
      selected: null,
    };
  },
  methods: {
    setSelected(img) {
      this.selected = img;
      this.$emit('imageSelected', img);
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
    .url-dwl {
        width: 50px;
        height: 50px;
        background: darkblue;
        font-size: 40px;
        color: white;
        transition-duration: 1s;
    }
    .url-dwl:hover {
        background-color: red;
        color: black;
    }
    .img-url {
        display: flex;
    }
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
