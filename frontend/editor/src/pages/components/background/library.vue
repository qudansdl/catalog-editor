<template>
  <div class="q-pa-md" style="height: 100%;">
      <div class="row q-col-gutter-xs fixed">
        <div class="col">
            <vue-tags-input
              v-model="tag"
              :tags="tags"
              :autocomplete-items="autocompleteItems"
              :add-only-from-autocomplete="true"
              @tags-changed="update"
            />
        </div>
      </div>
      <div class="row q-col-gutter-xs" style="padding-top: 35px; height: 100%;">
        <div class="col">
          <q-infinite-scroll @load="onLoad" :offset="90" style="height: 100%;" ref="loadArea">
            <vue-select-image
              :dataImages="backgrounds"
              :w="'250px'"
              :h="'200px'"
              @onselectimage="onSelectImage"/>
            <template v-slot:loading>
              <div class="row justify-center q-my-md">
                <q-spinner-dots color="primary" size="40px" />
              </div>
            </template>
          </q-infinite-scroll>
        </div>
      </div>
  </div>

</template>


<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { IBackgroundData, ICategoryData } from '@/api/types'
import ApiCategory from '@/api/categories';
import VueSelectImage from "@/components/VueSelectImage/VueSelectImage.vue";
import imageToDataUri from "@/utils/image-to-data-uri";
import ApiBackground from "@/api/backgrounds";
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

  private backgrounds: IBackgroundData[] = []
  private selected: IBackgroundData | null = null

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
    start: 1,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  tag = ''
  tags : any[] = []
  autocompleteItems: any[] = []

  onSelectImage(img: IBackgroundData) {
    this.selected = img;
    imageToDataUri(img.content!, (err: any, uri: any) => {
      this.$emit('imageSelected', uri);
    });
  }

  update(newTags: any[]) {
    this.autocompleteItems = [];
    this.tags = newTags;
    this.backgrounds = [];
    (this.$refs.loadArea as any).reset()
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

  private async onLoad(index: number, done: any) {
    console.log(`index ${index}`)
    this.listQuery.start = (index-1)*this.listQuery.length
    done(await this.getList())
  }

  private async getList() {
    this.isLoading = true
    const query = JSON.parse(JSON.stringify(this.listQuery))
    if (this.tags.length > 0) {
      query.columns.push({
        name: 'categories',
        operation: '',
        value: '',
        columns: [{
          name: 'id',
          operation: 'in',
          value: this.tags.map(c => c.id).join(',')
        }]
      })
    }
    const { data } = await ApiBackground.getBackgrounds(query)
    this.backgrounds = this.backgrounds.concat(data.backgrounds.data)
    this.isLoading = false
    return data.backgrounds.recordsFiltered < this.listQuery.start + this.listQuery.length
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
