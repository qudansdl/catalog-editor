<template>
  <div class="q-pa-md">
    <q-card>
      <div class="row q-col-gutter-xs">
        <div class="col">
          <q-card-section>
            <vue-tags-input
              v-model="tag"
              :tags="tags"
              :autocomplete-items="autocompleteItems"
              :add-only-from-autocomplete="true"
              @tags-changed="update"
            />
          </q-card-section>
        </div>
      </div>
      <div class="row q-col-gutter-xs">
        <div class="col">
          <q-card-section>
            <vue-select-image
              :dataImages="images"
              :w="'250px'"
              :h="'200px'"
              @onselectimage="onSelectImage"/>
          </q-card-section>
        </div>
      </div>
    </q-card>
  </div>

</template>


<script lang="ts">
  import { Component, Vue, Watch } from 'vue-property-decorator';
import {ICategoryData, IImageData, ITextData} from '@/api/types'
import ApiCategory from '@/api/categories';
import VueSelectImage from "@/components/VueSelectImage/VueSelectImage.vue";
import imageToDataUri from "@/utils/image-to-data-uri";
import ApiImage from "@/api/images";
import VueTagsInput from '@johmun/vue-tags-input';
import { Debounce } from 'vue-debounce-decorator';

@Component({
  components: {
    VueSelectImage,
    VueTagsInput
  }
})
export default class SelectImage extends Vue {
  private categories: ICategoryData[] = []

  private images: IImageData[] = []
  private selected: IImageData | null = null

  isLoading = false
  private categoryQuery = {
    start: 0,
    length: 0,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  private listQuery = {
    start: 0,
    length: 0,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  tag = ''
  tags : any[] = []
  autocompleteItems: any[] = []


  onSelectImage(img: IImageData) {
    this.selected = img;
    imageToDataUri(img.content!, (err: any, uri: any) => {
      this.$emit('imageSelected', uri);
    });
  }

  update(newTags: any[]) {
    this.autocompleteItems = [];
    this.tags = newTags;
    this.getList()
  }

  @Watch('tag')
  initItems() {
    if (this.tag.length < 2) return;

    this.loadItems(this.tag)
  }

  @Debounce(600)
  loadItems(tag: string) {
    this.getCategories(tag)
  }

  @Debounce(600)
  private async getList() {
    this.isLoading = true
    const query = JSON.parse(JSON.stringify(this.listQuery))
    if (this.categories.length > 0) {
      query.columns.push({
        name: 'categories',
        operation: '',
        value: '',
        columns: [{
          name: 'id',
          operation: 'in',
          value: this.categories.map(c => c.id).join(',')
        }]
      })
    }
    const { data } = await ApiImage.getImages(query)
    this.images = data.images.data
    this.isLoading = false
  }


  async getCategories (search: string) {
    this.isLoading = true
    const query = JSON.parse(JSON.stringify(this.categoryQuery))
    query.columns.push({
      name: 'name',
      operation: 'like',
      value: search
    })
    const { data } = await ApiCategory.getCategories(query)
    this.categories = data.categories.data

    this.autocompleteItems = this.categories.map(
      function(c, index, array){
        return { text: c.name, id: c.id }
      }
    )

    this.isLoading = false
  }
};
</script>
