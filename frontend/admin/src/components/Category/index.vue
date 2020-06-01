<template>
  <el-tree
    :data="[]"
    :props="props"
    @check="onCheck"
    :load="loadNode"
    ref="categoryTree"
    lazy
    show-checkbox
    check-strictly
  />
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import { ICategoryData } from '@/api/types'
import ApiCategory from '@/api/categories'

@Component({
  name: 'Category'
})
export default class extends Vue {
  @Prop({ required: true }) private value!: ICategoryData[]

  private props = {
    label: 'name',
    children: 'children',
    isLeaf: 'isLeaf'
  }

  private categories: ICategoryData[] = []

  private categoryQuery = {
    start: 0,
    length: 0,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  @Watch('categories')
  private onValueChange(value: ICategoryData[]) {
    this.$emit('changed', value)
    this.$emit('input', value)
  }

  private onCheck(node: any, treeInfo: any) {
    this.categories = treeInfo.checkedNodes
  }

  public getValue() {
    return this.categories
  }

  private async loadNode(node: any, resolve: any) {
    const parent = node.data
    if (parent.id) {
      const { data } = await ApiCategory.getCategory(parent.id)
      resolve(data.category.children)
    } else {
      const { data } = await this.getCategoryList()
      resolve(data.categories.data)
    }
  }

  private async getCategoryList() {
    const query = JSON.parse(JSON.stringify(this.categoryQuery))
    query.columns.push({
      name: 'parent',
      operation: 'null',
      value: ''
    })
    return ApiCategory.getCategories(query)
  }
}
</script>
