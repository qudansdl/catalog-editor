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
      <div class="row q-col-gutter-xs" style="padding-top: 35px;  height: 100%;">
        <div class="col">
          <q-infinite-scroll @load="onLoad" :offset="150" style="height: 100%;" ref="loadArea">
            <q-list bordered separator>
              <q-item
                clickable
                v-ripple
                v-for="text in texts"
                :key="text.id"
                @click="textSelected(text)"
                :class="selected && selected.id == text.id ? 'selected-item' : ''">
                <q-item-section>{{text.name}}</q-item-section>
              </q-item>
            </q-list>
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
import { ICategoryData, IImageData, ITextData } from '@/api/types';
import ApiCategory from '@/api/categories';
import ApiText from '@/api/texts';
import VueTagsInput from '@johmun/vue-tags-input';
import { Debounce } from 'vue-debounce-decorator';

@Component({
  components: {
    VueTagsInput
  }
})
export default class SelectText extends Vue {
  categories: ICategoryData[] = []

  private texts: ITextData[] = []
  private selected: ITextData | null = null

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

  textSelected (text: ITextData) {
    this.selected = text
    this.$emit('textSelected', text.content);
  }

  update(newTags: any[]) {
    this.autocompleteItems = [];
    this.tags = newTags;
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
    this.listQuery.start = (index-1) * this.listQuery.length
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

    const { data } = await ApiText.getTexts(query)
    this.texts = this.texts.concat(data.texts.data)
    this.isLoading = false
    return data.texts.recordsFiltered < this.listQuery.start + this.listQuery.length
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
