<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.columns[0].value"
        :placeholder="$t('background.name')"
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
        {{ $t('background.search') }}
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        {{ $t('background.add') }}
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
        :label="$t('background.id')"
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
        :label="$t('background.date')"
        width="180px"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.created | parseTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('background.name')"
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
        :label="$t('background.preview')"
        min-width="150px"
      >
        <template slot-scope="{row}">
          <el-image
            style="width: 100px; height: 100px"
            :src="row.content"
            :fit="'fill'"
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('background.actions')"
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
            {{ $t('background.edit') }}
          </el-button>
          <el-button
            v-if="row.status!=='deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            {{ $t('background.delete') }}
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
        :model="tempBackgroundData"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-input
          v-model="tempBackgroundData.parent"
          type="hidden"
        />
        <el-form-item
          :label="$t('background.name')"
          prop="name"
        >
          <el-input v-model="tempBackgroundData.name" />
        </el-form-item>
        <el-form-item
          :label="$t('background.category')"
          prop="categories"
        >
          <el-cascader
            ref="formCategory"
            :props="categoryProps"
            clearable
          />
        </el-form-item>
        <el-form-item
          :label="$t('background.file')"
          prop="file"
        >
          <el-button
            type="primary"
            @click="showUpload = true"
          >
            {{ $t('background.file') }}
          </el-button>
        </el-form-item>
        <el-form-item
          v-if="tempBackgroundData.content"
          :label="$t('background.preview')"
          prop="preview"
        >
          <el-image
            style="width: 100px; height: 100px"
            :src="tempBackgroundData.content"
            :fit="'fill'"
          />
        </el-form-item>
      </el-form>
      <image-crop-upload
        v-model="showUpload"
        :url="''"
        :width="width"
        :height="height"
        :lang-type="'ko'"
        :with-credentials="true"
        img-format="png"
        @src-file-set="srcFileSet"
        @crop-success="cropSuccess"
        @crop-upload-success="cropUploadSuccess"
        @crop-upload-fail="cropUploadFail"
      />
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogFormVisible = false">
          {{ $t('background.cancel') }}
        </el-button>
        <el-button
          type="primary"
          @click="dialogStatus==='create'?createData():updateData()"
        >
          {{ $t('background.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { Form, MessageBox } from 'element-ui'
import { cloneDeep } from 'lodash'
import ApiBackground, { defaultBackgroundData } from '@/api/backgrounds'
import { IBackgroundData, ICategoryData } from '@/api/types'
import Pagination from '@/components/Pagination/index.vue'
import ImageCropUpload from '@/components/vue-image-crop-upload/upload-2.vue'
import ApiCategory from '@/api/categories'

@Component({
  name: 'BackgroundTable',
  components: {
    Pagination,
    ImageCropUpload
  }
})
export default class extends Vue {
  @Prop({ default: 100 }) private width!: number
  @Prop({ default: 100 }) private height!: number

  private showUpload = false

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
  private list: IBackgroundData[] = []
  private total = 0
  private listLoading = true

  private listQuery: any = {
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

  private tempBackgroundData = defaultBackgroundData

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

    const { data } = await ApiBackground.getBackgrounds(query)

    this.list = data.backgrounds.data
    this.total = data.backgrounds.recordsFiltered
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

  private resetTempBackgroundData() {
    this.tempBackgroundData = cloneDeep(defaultBackgroundData)
  }

  private handleCreate() {
    this.resetTempBackgroundData()
    this.dialogStatus = 'create'
    this.dialogFormVisible = true
    this.$nextTick(() => {
      (this.$refs.dataForm as Form).clearValidate()
    })
  }

  private createData() {
    (this.$refs.dataForm as Form).validate(async(valid) => {
      if (valid) {
        const selectedNodes = (this.$refs.formCategory as any).getCheckedNodes()
        const selectedCategories = selectedNodes.map((n: any) => n.data)

        const { data } = await ApiBackground.createBackground(
          this.tempBackgroundData.name,
          this.tempBackgroundData.content!,
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
    this.tempBackgroundData = Object.assign({}, row)
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

        const tempData = Object.assign({}, this.tempBackgroundData)
        const { data } = await ApiBackground.updateBackground(
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
      const { data } = await ApiBackground.deleteBackground(row.id)
      this.$notify({
        title: '성공',
        message: '삭제 했습니다',
        type: 'success',
        duration: 2000
      })
      await this.getList()
    })
  }

  private srcFileSet(fileName: string, fileType: string, fileSize: number) {
    this.$emit('src-file-set', fileName, fileType, fileSize)
  }

  private cropSuccess(imgDataUrl: string, field: string) {
    this.tempBackgroundData.content = imgDataUrl
    this.$emit('crop-success', imgDataUrl, field)
  }

  private cropUploadSuccess(jsonData: any, field: string) {
    this.$emit('crop-upload-success', jsonData, field)
  }

  private cropUploadFail(status: boolean, field: string) {
    this.$emit('crop-upload-fail', status, field)
  }
}
</script>
