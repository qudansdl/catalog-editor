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
    <el-row>
      <ApolloQuery :query="QUERY_CATEGORIES">
        <el-col :span="8" v-for="(o, index) in 2" :key="o" :offset="index > 0 ? 2 : 0">
          <el-card :body-style="{ padding: '0px' }">
            <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
            <div style="padding: 14px;">
              <span>Yummy hamburger</span>
              <div class="bottom clearfix">
                <time class="time">{{ currentDate }}</time>
                <el-button type="text" class="button">Operating</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </ApolloQuery>
    </el-row>
    <el-pagination
            background
            layout="prev, pager, next"
            :total="1000">
    </el-pagination>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { GET_CATEGORIES } from "@/graphql/category";
import { GET_IMAGES } from "@/graphql/image";

@Component({
  name: 'SelectImage',
})
export default class extends Vue {
  private category: string = ''

  private QUERY_CATEGORIES = GET_CATEGORIES
  private QUERY_IMAGES = GET_IMAGES
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
