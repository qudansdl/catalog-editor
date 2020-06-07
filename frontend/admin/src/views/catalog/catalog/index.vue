<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.columns[0].value"
        :placeholder="$t('catalog.name')"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-cascader
        v-model="categories"
        :props="categoryProps"
        clearable
      />
      <el-select
        v-model="listQuery.order[0].dir"
        style="width: 140px"
        class="filter-item"
        @change="handleFilter"
      >
        <el-option
          v-for="item in sortOptions"
          :key="item.key"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        {{ $t('catalog.search') }}
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        {{ $t('catalog.add') }}
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column
        :label="$t('catalog.id')"
        prop="id"
        sortable="custom"
        align="center"
        width="80"
        :class-name="getSortClass('id')"
      >
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('catalog.date')"
        width="180px"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.created | parseTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('catalog.name')"
        min-width="150px"
      >
        <template slot-scope="{row}">
          <span
            class="link-type"
            @click="handleUpdate(row)"
          >{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('catalog.thumbnail')"
        min-width="150px"
      >
        <template slot-scope="{row}">
          <el-image
            :src="row.thumbnail"
            :fit="'fill'"></el-image>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('catalog.actions')"
        align="center"
        width="230"
        class-name="fixed-width"
      >
        <template slot-scope="{row, $index}">
          <el-button
            type="secondary"
            size="mini"
            @click="handleDownload(row)"
          >
            {{ $t('catalog.download') }}
          </el-button>
          <el-button
            v-if="row.status!=='deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            {{ $t('catalog.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.length"
      @pagination="getList"
    />

  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { Form, MessageBox } from 'element-ui'
import { cloneDeep } from 'lodash'
import ApiCatalog, { defaultCatalogData } from '@/api/catalogs'
import { ICategoryData, ICatalogData } from '@/api/types'
import Pagination from '@/components/Pagination/index.vue'
import ApiCategory from '@/api/categories'

@Component({
  name: 'CatalogTable',
  components: {
    Pagination
  }
})
export default class extends Vue {
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

  private categoryProps = {
    value: 'id',
    label: 'name',
    children: 'children',
    leaf: 'isLeaf',
    lazy: true,
    multiple: true,
    checkStrictly: true,
    lazyLoad: this.loadNode
  }

  private tableKey = 0
  private list: ICatalogData[] = []
  private total = 0
  private listLoading = true

  private listQuery = {
    page: 1,
    start: 1,
    length: 20,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: [{
      name: 'name',
      operation: 'like',
      value: '',
      columns: []
    }]
  }

  private sortOptions = [
    { label: 'Date Ascending', key: '+date', value: 'desc' },
    { label: 'Date Descending', key: '-date', value: 'asc' }
  ]

  created() {
    this.getList()
  }

  private async loadNode(node: any, resolve: any) {
    if (node.root) {
      const { data } = await this.getCategoryList()
      resolve(data.categories.data)
    } else {
      const rslt = await ApiCategory.getCategory(node.data.id)
      resolve(rslt.data.category.children)
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

  private async getList() {
    this.listLoading = true
    this.listQuery.start = (this.listQuery.page - 1) * this.listQuery.length

    const query = JSON.parse(JSON.stringify(this.listQuery))
    delete query.page
    if (this.categories.length > 0) {
      query.columns.push({
        name: 'categories',
        operation: '',
        value: '',
        columns: [{
          name: 'id',
          operation: 'in',
          value: this.categories.join(',')
        }]
      })
    }

    const { data } = await ApiCatalog.getCatalogs(query)

    this.list = data.catalogs.data
    this.total = data.catalogs.recordsFiltered
    this.listLoading = false
  }

  private handleFilter() {
    this.listQuery.page = 1
    this.getList()
  }

  private sortChange(data: any) {
    const { prop, order } = data
    if (prop === 'id') {
      this.sortByID(order)
    }
  }

  private sortByID(order: string) {
    if (order === 'ascending') {
      this.listQuery.order[0].dir = 'asc'
    } else {
      this.listQuery.order[0].dir = 'desc'
    }
    this.handleFilter()
  }

  private getSortClass(key: string) {
    const sort = this.listQuery.order[0].dir
    return sort === 'asc' ? 'ascending' : 'descending'
  }

  private handleDownload(row: any) {
    const link = document.createElement('a')
    link.download = name
    link.href = row.image
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }

  private async handleDelete(row: any, index: number) {
    MessageBox.confirm(
      '삭제하시겠습니까?',
      '삭제확인',
      {
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
        type: 'warning'
      }
    ).then(async() => {
      const { data } = await ApiCatalog.deleteCatalog(row.id)
      this.$notify({
        title: '성공',
        message: '삭제 했습니다',
        type: 'success',
        duration: 2000
      })
      await this.getList()
    })
  }
}
</script>
