<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-layout view="Lhh lpR fff" container>
      <q-footer>
        <q-toolbar inset>
          <q-btn color="primary" label="적용" @click="apply" :disable="template == null"/>
          <q-btn color="brown-5" label="닫기" @click="showDialog = false"/>
        </q-toolbar>
      </q-footer>

      <q-page-container>
        <q-page padding>
          <q-card>
            <div class="row q-col-gutter-xs fixed">
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
            <div class="row q-col-gutter-xs" style="padding-top: 35px">
              <div class="col">
                <q-card-section>
                  <q-list bordered separator>
                    <q-item clickable v-ripple v-for="template in templates" :key="template.id" @click="templateSelected(template)">
                      <q-item-section>{{template.name}}</q-item-section>
                    </q-item>
                  </q-list>
                </q-card-section>
              </div>
            </div>
          </q-card>
        </q-page>
      </q-page-container>
    </q-layout>
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

  private template: ITemplateData | null = null
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

  mounted()
  {
    this.getList();
  }


  apply() {
    this.$emit('apply', this.template);
    this.showDialog = false;
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
    this.template = template
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
    const { data } = await ApiTemplate.getTemplates(query)

    this.templates = data.templates.data

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
}
</script>
