<template>
  <div class="select-container">
      <el-select v-model="category" placeholder="Select">
        <el-option :value="''" :label="'전체'"> </el-option>
        <ApolloQuery :query="QUERY_CATEGORIES">
          <template v-slot="{ result: { loading, error, data } }">
            <template v-if="data && data.categories.data">
              <el-option
                      v-for="category in data.categories.data"
                      :key="category.id"
                      :label="category.name"
                      :value="category.id">
              </el-option>
            </template>
          </template>
        </ApolloQuery>
      </el-select>


    <ApolloQuery
            fetch-policy=""
            :query="QUERY_IMAGES"
            :variables="variables()"
            ref="imageApollo"
    >
      <template v-slot="{ result: { loading, error, data } }">
        <template v-if="data && data.images.data">
          <el-row>
            <el-col :span="8" v-for="image in data.images.data" :key="image.id">
              <el-card :body-style="{ padding: '0px' }">
                <el-image style="width: 100px; height: 100px" :src="image.content"></el-image>
              </el-card>
            </el-col>
          </el-row>
          <el-pagination
                  background
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page.sync="currentPage"
                  :page-sizes="[10, 20, 30, 40]"
                  :page-size="pageSize"
                  layout="prev, pager, next"
                  :total="data.images.recordsTotal">
          </el-pagination>

        </template>
      </template>
    </ApolloQuery>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { GET_CATEGORIES } from "@/graphql/category";
import { GET_IMAGES, getImageVariable } from "@/graphql/image";

@Component({
  name: 'SelectImage'
})
export default class extends Vue {
  private category: string = ''
  private pageSize = 10
  private currentPage = 1

  private QUERY_CATEGORIES = GET_CATEGORIES
  private QUERY_IMAGES = GET_IMAGES

  private variables() {
    return getImageVariable(this.category, this.pageSize, this.currentPage);
  }

  private handleSizeChange(val: number) {
      this.pageSize = val
      console.log(this.$refs.imageApollo)
  }
  private handleCurrentChange(val: number) {
    this.currentPage = val
    console.log(this.$refs.imageApollo)
  }

}
</script>

<style lang="scss" scoped>

.select-container {
  width: 100%;
  position: relative;
  @include clearfix;

  .image-uploader {
    width: 100%;
    float: left;
  }

  .image-preview {
    width: 100%;
    position: relative;
    border: 1px dashed #d9d9d9;
    float: right;

    .image-preview-wrapper {
      position: relative;
      width: 100%;
      height: 300px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .image-preview-action {
      position: absolute;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      cursor: default;
      text-align: center;
      color: #fff;
      opacity: 0;
      font-size: 20px;
      background-color: rgba(0, 0, 0, .5);
      transition: opacity .3s;
      cursor: pointer;
      text-align: center;
      line-height: 200px;

      .el-icon-delete {
        font-size: 36px;
      }
    }

    &:hover {
      .image-preview-action {
        opacity: 1;
      }
    }
  }
}
</style>
