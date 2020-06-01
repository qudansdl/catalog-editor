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
            <q-list bordered separator>
              <q-item clickable v-ripple v-for="text in texts" :key="text.id">
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
import { Component, Vue } from 'vue-property-decorator';
import {ICategoryData, ITextData} from '@/api/types'
import ApiCategory from '@/api/categories';
import ApiText from '@/api/texts';

@Component
export default class SelectText extends Vue {
  selectedCategories: ICategoryData[] = []

  categories: ICategoryData[] = []

  private texts: ITextData[] = {}

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
    this.isLoading = false
  }

  clearAll () {
    this.selectedCategories = []
  }
};
</script>
