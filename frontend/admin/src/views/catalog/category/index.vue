<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.columns[0].value"
        :placeholder="$t('category.name')"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
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
        {{ $t('category.search') }}
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        {{ $t('category.add') }}
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
        :label="$t('category.id')"
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
        :label="$t('category.date')"
        width="180px"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.created | parseTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('category.name')"
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
        :label="$t('category.parent')"
        min-width="150px"
      >
        <template slot-scope="{row}">
          <span v-if="row.parent">{{ row.parent.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('category.actions')"
        align="center"
        width="230"
        class-name="fixed-width"
      >
        <template slot-scope="{row, $index}">
          <el-button
            type="primary"
            size="mini"
            @click="handleUpdate(row)"
          >
            {{ $t('category.edit') }}
          </el-button>
          <el-button
            type="secondary"
            size="mini"
            @click="handleCreateChild(row)"
          >
            {{ $t('category.child.add') }}
          </el-button>
          <el-button
            v-if="row.status!=='deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            {{ $t('category.delete') }}
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

    <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="dialogFormVisible"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="tempCategoryData"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-input
          v-model="tempCategoryData.parent"
          type="hidden"
        />
        <el-form-item
          :label="$t('category.name')"
          prop="name"
        >
          <el-input v-model="tempCategoryData.name" />
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogFormVisible = false">
          {{ $t('category.cancel') }}
        </el-button>
        <el-button
          type="primary"
          @click="dialogStatus==='create'?createData():updateData()"
        >
          {{ $t('category.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { Form, MessageBox } from 'element-ui'
import { cloneDeep } from 'lodash'
import ApiCategory, { defaultCategoryData } from '@/api/categories'
import { ICategoryData } from '@/api/types'
import Pagination from '@/components/Pagination/index.vue'

@Component({
  name: 'CategoryTable',
  components: {
    Pagination
  }
})
export default class extends Vue {
  private tableKey = 0
  private list: ICategoryData[] = []
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
      operation: 'eq',
      value: ''
    }]
  }

  private sortOptions = [
    { label: 'Date Ascending', key: '+date', value: 'desc' },
    { label: 'Date Descending', key: '-date', value: 'asc' }
  ]

  private dialogFormVisible = false
  private dialogStatus = ''
  private textMap = {
    update: 'Edit',
    create: 'Create'
  }

  private rules = {
    name: [{ required: true, message: 'name is required', trigger: 'change' }]
  }

  private tempCategoryData = defaultCategoryData

  created() {
    this.getList()
  }

  private async getList() {
    this.listLoading = true
    this.listQuery.start = (this.listQuery.page - 1) * this.listQuery.length

    const query = Object.assign({}, this.listQuery)
    delete query.page

    const { data } = await ApiCategory.getCategories(query)

    this.list = data.categories.data
    this.total = data.categories.recordsFiltered
    // Just to simulate the time of the request
    setTimeout(() => {
      this.listLoading = false
    }, 0.5 * 1000)
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

  private resetTempCategoryData() {
    this.tempCategoryData = cloneDeep(defaultCategoryData)
  }

  private handleCreateChild(row: any) {
    this.resetTempCategoryData()
    this.tempCategoryData.parent = row.id
    this.dialogStatus = 'create'
    this.dialogFormVisible = true
    this.$nextTick(() => {
      (this.$refs.dataForm as Form).clearValidate()
    })
  }

  private handleCreate() {
    this.resetTempCategoryData()
    this.dialogStatus = 'create'
    this.dialogFormVisible = true
    this.$nextTick(() => {
      (this.$refs.dataForm as Form).clearValidate()
    })
  }

  private createData() {
    (this.$refs.dataForm as Form).validate(async(valid) => {
      if (valid) {
        const { data } = await ApiCategory.createCategory(
          this.tempCategoryData.name,
          this.tempCategoryData.parent
        )
        this.getList()
        this.dialogFormVisible = false
        this.$notify({
          title: '성공',
          message: '추가 했습니다',
          type: 'success',
          duration: 2000
        })
      }
    })
  }

  private handleUpdate(row: any) {
    this.tempCategoryData = Object.assign({}, row)
    this.dialogStatus = 'update'
    this.dialogFormVisible = true
    this.$nextTick(() => {
      (this.$refs.dataForm as Form).clearValidate()
    })
  }

  private updateData() {
    (this.$refs.dataForm as Form).validate(async(valid) => {
      if (valid) {
        const tempData = Object.assign({}, this.tempCategoryData)
        const { data } = await ApiCategory.updateCategory(tempData)
        this.dialogFormVisible = false
        this.$notify({
          title: '성공',
          message: '저장했습니다',
          type: 'success',
          duration: 2000
        })
        this.getList()
      }
    })
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
      const { data } = await ApiCategory.deleteCategory(row.id)
      this.getList()
      this.$notify({
        title: '성공',
        message: '삭제 했습니다',
        type: 'success',
        duration: 2000
      })
    })
  }
}
</script>
