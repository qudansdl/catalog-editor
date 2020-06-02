<template>
  <div class="q-pa-md">
    <q-card>
      <div class="row q-col-gutter-xs">
        <div class="col">
          <q-card-section>
            <multiselect
              v-model="selectedCategories"
              id="ajax"
              label="name"
              track-by="id"
              placeholder="Type to search"
              open-direction="bottom"
              :options="categories"
              :multiple="true"
              :searchable="true"
              :loading="isLoading"
              :internal-search="false"
              :clear-on-select="false"
              :close-on-select="false"
              :options-limit="300"
              :limit="10"
              :limit-text="limitText"
              :max-height="600"
              :show-no-results="false"
              :hide-selected="true"
              @select="getList"
              @search-change="getCategories">
              <template slot="tag" slot-scope="{ option, remove }"><span class="custom__tag"><span>{{ option.name }}</span><span class="custom__remove" @click="remove(option)">❌</span></span></template>
              <template slot="clear" slot-scope="props">
                <div class="multiselect__clear" v-if="selectedCategories.length" @mousedown.prevent.stop="clearAll(props.search)"></div>
              </template><span slot="noResult">Oops! No elements found. Consider changing the search query.</span>
            </multiselect>
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
import { Component, Vue } from 'vue-property-decorator';
import {ICategoryData, IImageData, ITextData} from '@/api/types'
import ApiCategory from '@/api/categories';
import VueSelectImage from "@/components/VueSelectImage/VueSelectImage.vue";
import imageToDataUri from "@/utils/image-to-data-uri";
import ApiImage from "@/api/images";

@Component({
  components: {
    VueSelectImage
  }
})
export default class SelectImage extends Vue {
  private selectedCategories: ICategoryData[] = []

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

  limitText (count: number) {
    return ` ${count} 선택됨`
  }

  onSelectImage(img: IImageData) {
    this.selected = img;
    imageToDataUri(img.content!, (err: any, uri: any) => {
      this.$emit('imageSelected', uri);
    });
  }

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
    this.isLoading = false
  }

  clearAll () {
    this.selectedCategories = []
  }
};
</script>
