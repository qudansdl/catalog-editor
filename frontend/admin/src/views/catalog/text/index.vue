<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.columns[0].value"
        :placeholder="$t('text.name')"
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
        {{ $t('text.search') }}
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        {{ $t('text.add') }}
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
        :label="$t('text.id')"
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
        :label="$t('text.date')"
        width="180px"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.created | parseTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('text.name')"
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
        :label="$t('text.content')"
        min-width="150px"
      >
        <template slot-scope="{row}">
          <span>{{ row.content }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('text.actions')"
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
            {{ $t('text.edit') }}
          </el-button>
          <el-button
            v-if="row.status!=='deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            {{ $t('text.delete') }}
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
      width="75%"
      :title="textMap[dialogStatus]"
      :visible.sync="dialogFormVisible"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="tempTextData"
        label-position="left"
        label-width="100px"
        style="width: 90%; margin-left:10px;"
      >
        <el-input
          v-model="tempTextData.parent"
          type="hidden"
        />
        <el-form-item
          :label="$t('text.name')"
          prop="name"
        >
          <el-input v-model="tempTextData.name" />
        </el-form-item>
        <el-form-item
          :label="$t('text.category')"
          prop="categories"
        >
          <el-cascader
            ref="formCategory"
            :props="categoryProps"
            clearable
          />
        </el-form-item>

        <el-form-item
          :label="$t('text.content')"
          prop="content"
        >
          <quill-editor
            :content="tempTextData.content"
            @change="onEditorChange($event)"
          />
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogFormVisible = false">
          {{ $t('text.cancel') }}
        </el-button>
        <el-button
          type="primary"
          @click="dialogStatus==='create'?createData():updateData()"
        >
          {{ $t('text.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { Form, MessageBox } from 'element-ui'
import { cloneDeep } from 'lodash'
import ApiText, { defaultTextData } from '@/api/texts'
import { ICategoryData, ITextData } from '@/api/types'
import Pagination from '@/components/Pagination/index.vue'
import Tinymce from '@/components/Tinymce/index.vue'
import ApiCategory from '@/api/categories'

@Component({
  name: 'TextTable',
  components: {
    Pagination,
    Tinymce
  }
})
export default class extends Vue {
  private tinymceActive = true

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
  private list: ITextData[] = []
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

  private dialogFormVisible = false
  private dialogStatus = ''
  private textMap = {
    update: '수정',
    create: '생성'
  }

  private rules = {
    name: [{ required: true, message: 'name is required', trigger: 'change' }]
  }

  private tempTextData = defaultTextData

  created() {
    this.getList()
  }

  deactivated() {
    this.tinymceActive = false
  }

  activated() {
    this.tinymceActive = true
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

    console.log('aaaaa')
    const query = JSON.parse(JSON.stringify(this.listQuery))
    console.log('aaaaa', query)
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

    const { data } = await ApiText.getTexts(query)

    this.list = data.texts.data
    this.total = data.texts.recordsFiltered
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

  private resetTempTextData() {
    this.tempTextData = cloneDeep(defaultTextData)
  }

  private handleCreate() {
    this.resetTempTextData()
    this.dialogStatus = 'create'
    this.dialogFormVisible = true
    this.$nextTick(() => {
      (this.$refs.dataForm as Form).clearValidate()
    })
  }

  onEditorChange(quill: any, html: string, text: string) {
    console.log('editor change!', quill, html, text)
    this.tempTextData.content = html
  }

  private createData() {
    (this.$refs.dataForm as Form).validate(async(valid) => {
      if (valid) {
        const selectedNodes = (this.$refs.formCategory as any).getCheckedNodes()
        const selectedCategories = selectedNodes.map((n: any) => n.data)

        const { data } = await ApiText.createText(
          this.tempTextData.name,
          this.tempTextData.content!,
          selectedCategories
        )
        this.dialogFormVisible = false
        this.$notify({
          title: '성공',
          message: '추가 했습니다',
          type: 'success',
          duration: 2000
        })
        await this.getList()
      }
    })
  }

  private handleUpdate(row: any) {
    this.tempTextData = Object.assign({}, row)
    this.dialogStatus = 'update'
    this.dialogFormVisible = true
    this.$nextTick(() => {
      (this.$refs.dataForm as Form).clearValidate()
    })
  }

  private updateData() {
    (this.$refs.dataForm as Form).validate(async(valid) => {
      if (valid) {
        const selectedNodes = (this.$refs.formCategory as any).getCheckedNodes()
        const selectedCategories = selectedNodes.map((n: any) => n.data)

        const tempData = Object.assign({}, this.tempTextData)
        debugger
        const { data } = await ApiText.updateText(
          tempData.id!,
          tempData.name,
          tempData.content!,
          selectedCategories
        )
        this.dialogFormVisible = false
        this.$notify({
          title: '성공',
          message: '저장했습니다',
          type: 'success',
          duration: 2000
        })
        await this.getList()
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
      const { data } = await ApiText.deleteText(row.id)
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
