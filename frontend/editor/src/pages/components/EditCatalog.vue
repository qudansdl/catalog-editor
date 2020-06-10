<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-card>
      <q-card-section class="row justify-center scroll" style="min-height: 250px;width: 100%">
                <q-infinite-scroll @load="onLoad" :offset="150" ref="loadArea">
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
      </q-card-section>

      <q-separator />

      <q-card-actions class="fixed-bottom bg-grey-3">
        <q-btn flat label="적용" @click="apply" :disable="selected == null"/>
        <q-btn flat label="닫기" @click="showDialog = false" />
      </q-card-actions>
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

  private selected: ICatalogData | null = null
  private catalogs: ICatalogData[] = []

  isLoading = false

  private listQuery = {
    start: 0,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }


  async apply() {
    const { data } = await ApiCatalog.getCatalog(this.selected!.id!)
    this.$emit('apply', data.catalog);
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


  private async onLoad(index: number, done: any) {
    if(index == 1)
    {
      this.catalogs = []
    }
    console.log(`index ${index}`)
    this.listQuery.start = (index -1) * this.listQuery.length
    done(await this.getList())
  }

  private async getList() {
    this.isLoading = true
    const query = JSON.parse(JSON.stringify(this.listQuery))
    const { data } = await ApiCatalog.getCatalogs(query)
    this.catalogs = this.catalogs.concat(data.catalogs.data)
    this.isLoading = false
    return data.catalogs.recordsFiltered < this.listQuery.start + this.listQuery.length
  }

}
</script>
