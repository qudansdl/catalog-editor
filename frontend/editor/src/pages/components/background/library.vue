<template>
  <q-card>
    <q-card-section class="row justify-center">
            <vue-tags-input
              placeholder="카테고리 입력"
              v-model="tag"
              :tags="tags"
              :autocomplete-items="autocompleteItems"
              :add-only-from-autocomplete="true"
              @tags-changed="update"
              style="width: 100%"
            />
    </q-card-section>

    <q-separator />

    <q-card-section class="row justify-center">
      <q-infinite-scroll @load="onLoad" :offset="90" style="height: 100%;" ref="loadArea">
        <vue-select-image
          :dataImages="backgrounds"
          :w="'250px'"
          :h="'200px'"
          @onselectimage="onSelectImage"/>
        <template v-slot:loading>
          <div class="row justify-center">
            <q-spinner-dots color="primary" size="40px" />
          </div>
        </template>
      </q-infinite-scroll>
    </q-card-section>
  </q-card>
</template>


<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { IBackgroundData, ICategoryData } from '@/api/types'
import ApiCategory from '@/api/categories';
import VueSelectImage from '@/components/VueSelectImage/VueSelectImage.vue';
import imageToDataUri from '@/utils/image-to-data-uri';
import ApiBackground from '@/api/backgrounds';
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

  start = 0
  length = 10

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
    if (this.tag.length < 1) return;

    this.loadItems(this.tag)
  }

  @Debounce(600)
  loadItems(tag: string) {
    this.getCategories(tag)
  }

  private async onLoad(index: number, done: any) {
    if(index == 1)
    {
      this.backgrounds = []
    }
    console.log(`index ${index}`)
    this.start = (index-1)*this.length
    done(await this.getList())
  }

  private async getList() {
    this.isLoading = true

    const { data } = await ApiBackground.getBackgrounds(this.start, this.length, this.tags)
    this.backgrounds = this.backgrounds.concat(data.backgrounds.data)
    this.isLoading = false
    return data.backgrounds.recordsFiltered < this.start + this.length
  }


  async getCategories (search: string) {
    this.isLoading = true

    const { data } = await ApiCategory.getCategories(search)
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
