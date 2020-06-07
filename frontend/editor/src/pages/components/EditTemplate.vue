<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-card>
      <q-card-section class="row justify-center q-my-md">
                  <vue-tags-input
                    placeholder="카테고리 입력"
                    v-model="tag"
                    :tags="tags"
                    :autocomplete-items="autocompleteItems"
                    :add-only-from-autocomplete="true"
                    @tags-changed="update"
                  />
      </q-card-section>

      <q-separator />

      <q-card-section class="q-pt-none scroll"  style="max-height: 50vh;min-height: 250px;">
                <q-infinite-scroll @load="onLoad" :offset="150" style="height: 100%;" ref="loadArea">
                  <q-list bordered separator>
                    <q-item
                      clickable
                      v-ripple
                      v-for="template in templates"
                      :key="template.id"
                      @click="templateSelected(template)"
                      :class="selected && selected.id == template.id ? 'selected-item' : ''">
                      <q-item-section>{{template.name}}</q-item-section>
                    </q-item>
                  </q-list>
                </q-infinite-scroll>
      </q-card-section>

      <q-separator />

      <q-card-actions align="right" class="text-primary">
        <q-btn flat label="적용" @click="apply" :disable="selected == null"/>
        <q-btn flat label="닫기" @click="showDialog = false" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script lang="ts">
import { Vue, Component, Prop, Watch } from 'vue-property-decorator';
import { cloneDeep } from 'lodash';
import { ICategoryData, ITemplateData, ITextData } from '@/api/types';
import { Debounce } from 'vue-debounce-decorator';
import ApiTemplate from '@/api/templates';
import ApiCategory from '@/api/categories';
import VueTagsInput from '@johmun/vue-tags-input';

@Component({
  components: {
    VueTagsInput
  }
})
export default class EditTemplate extends Vue {
  @Prop({ required: true }) private value!: any;

  private maximizedToggle = true;

  categories: ICategoryData[] = []

  private selected: ITemplateData | null = null
  private templates: ITemplateData[] = []

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

  templateSelected (template: ITemplateData) {
    this.selected = template
  }

  update(newTags: any[]) {
    this.autocompleteItems = [];
    this.tags = newTags;
    this.templates = [];
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
    const { data } = await ApiTemplate.getTemplates(query)
    this.templates = this.templates.concat(data.templates.data)
    this.isLoading = false
    return data.templates.recordsFiltered < this.listQuery.start + this.listQuery.length
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
