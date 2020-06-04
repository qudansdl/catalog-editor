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
            <q-list bordered separator>
              <q-item clickable v-ripple v-for="text in texts" :key="text.id" @click="textSelected(text)">
                <q-item-section>{{text.content}}</q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </div>
      </div>
    </q-card>
  </div>
</template>

<script lang="ts">
  import { Component, Vue, Watch } from 'vue-property-decorator';
import {ICategoryData, ITextData} from '@/api/types'
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

  textSelected (text: ITextData) {
    this.$emit('textSelected', text.content);
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

    this.texts = data.texts.data

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
