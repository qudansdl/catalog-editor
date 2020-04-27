<template>
  <div class="select-container">
      <el-select v-model="category" placeholder="Select">
        <el-option :value="''" :label="'전체'"> </el-option>
        <el-option
          v-for="category in categories"
          :key="category.id"
          :label="category.name"
          :value="category.id">
        </el-option>
      </el-select>

      <el-row>
        <el-col :span="8" v-for="image in images" :key="image.id" :class="[image.id == selected ? 'selected' : 'not-selected']">
           <el-card :body-style="{ padding: '0px' }">
                <el-image style="width: 100px; height: 100px" :src="image.content"  @click="selectImage(image)"></el-image>
           </el-card>
        </el-col>
      </el-row>
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage"
        :page-sizes="[5, 10, 20, 30, 40]"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="recordsFiltered">
      </el-pagination>
  </div>
</template>

<script lang="ts">
import { Component, Model, Vue, Watch } from 'vue-property-decorator'
import { getImages  } from '@/api/images'
import { getCategories  } from '@/api/categories'
import { IImage } from "@/api/types";

@Component({
  name: 'SelectImage'
})
export default class extends Vue {
  @Model('imageUrl', { type: String }) imageUrl: string = ''

  private category: string = ''
  private selected: string = ''
  private pageSize = 5
  private currentPage = 1
  private recordsFiltered = 0

  private categories = []
  private images = []

  created() {
    this.fetchData()
  }


  selectImage (image: IImage) {
    this.selected = image.id
    this.$emit('imageUrl', image.content)
  }

  private async fetchData() {

    {
      const {data} = await getCategories()
      this.categories = data.categories.data
      if(this.categories.length > 0)
      {
        this.category = ''
      }
    }

    await this.loadImages()
  }

  private async loadImages() {
    const {data}  = await getImages(this.category, this.pageSize, this.currentPage)
    this.images = data.images.data
    this.recordsFiltered = data.images.recordsFiltered
  }

  @Watch('category', { immediate: true, deep: true })
  handleCategoryChange(val: string, oldVal: string) {
    this.loadImages()
  }

  private handleSizeChange(val: number) {
    this.pageSize = val
    this.loadImages()
  }
  private handleCurrentChange(val: number) {
    this.currentPage = val
    this.loadImages()
  }

}
</script>

<style lang="scss" scoped>

.select-container {
  width: 100%;
  position: relative;
  @include clearfix;

  .not-selected {
    padding: 1px;
  }

  .selected {
    border-width: 1px;
    border-color: #444;
    border-style: dotted;
  }
}
</style>
