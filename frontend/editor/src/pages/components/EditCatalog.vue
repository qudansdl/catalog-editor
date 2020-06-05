<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-card>
      <q-card-section style="height: 100%">
    <q-layout view="Lhh lpR fff" container>
      <q-page-container>
        <q-page padding>
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
            <div class="row q-col-gutter-xs" style="padding-top: 35px">
              <div class="col">
                <q-infinite-scroll @load="onLoad" :offset="150" style="height: 100%;" ref="loadArea">
                  <q-list bordered separator>
                    <q-item
                      clickable
                      v-ripple
                      v-for="catalog in catalogs"
                      :key="catalog.id"
                      @click="catalogSelected(catalog)"
                      :class="selected && selected.id == catalog.id ? 'selected-item' : ''">
                      <q-item-section>{{catalog.name}}</q-item-section>
                    </q-item>
                  </q-list>
                </q-infinite-scroll>
              </div>
            </div>
        </q-page>
        <q-footer>
          <q-toolbar inset>
            <q-btn color="primary" label="적용" @click="apply" :disable="selected == null"/>
            <q-btn color="brown-5" label="닫기" @click="showDialog = false"/>
          </q-toolbar>
        </q-footer>
      </q-page-container>
    </q-layout>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>
<script lang="ts">
import { Vue, Component, Prop, Watch } from 'vue-property-decorator';
import { cloneDeep } from 'lodash';
import { ICategoryData, ICatalogData, ITextData } from '@/api/types';
import { Debounce } from 'vue-debounce-decorator';
import ApiCatalog from '@/api/catalogs';
import ApiCategory from '@/api/categories';
import VueTagsInput from '@johmun/vue-tags-input';

@Component({
  components: {
    VueTagsInput
  }
})
export default class EditCatalog extends Vue {
  @Prop({ required: true }) private value!: any;

  private maximizedToggle = true;

  categories: ICategoryData[] = []

  private selected: ICatalogData | null = null
  private catalogs: ICatalogData[] = []

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

  apply() {
    this.$emit('apply', this.selected);
    this.showDialog = false;
    this.selected = null
  }

  get content() {
    return this.value.content;
  }

  set content(content) {
    const newValue = cloneDeep(this.value);
    newValue.content = content;

    this.$emit('input', newValue);
  }

  get showDialog() {
    return this.value.show;
  }

  set showDialog(show) {
    const newValue = cloneDeep(this.value);
    newValue.show = show;

    this.$emit('input', newValue);
  }

  catalogSelected (catalog: ICatalogData) {
    this.selected = catalog
  }

  update(newTags: any[]) {
    this.autocompleteItems = [];
    this.tags = newTags;
    this.catalogs = [];
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
    this.listQuery.start = (index -1) * this.listQuery.length
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
    const { data } = await ApiCatalog.getCatalogs(query)
    this.catalogs = this.catalogs.concat(data.catalogs.data)
    this.isLoading = false
    return data.catalogs.recordsFiltered < this.listQuery.start + this.listQuery.length
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
}
</script>
