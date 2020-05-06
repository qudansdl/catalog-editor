<template>
  <div class="upload-container">
    <el-row>
      <el-col :span="24">
        <el-upload
                :multiple="false"
                :showFileList="false"
                :httpRequest="uploadImage"
                action=""
                class="image-uploader"
                drag
        >
          <i class="el-icon-upload" />
          <div class="el-upload__text">
            파일을 여기로 드래그하거나 <em> 클릭하여 업로드 </em>
          </div>
        </el-upload>
      </el-col>
    </el-row>
    <el-row v-show="imageUrl.length>1">
      <el-col :span="24">
        <div class="image-preview">
          <div class="image-preview-wrapper">
            <img :src="imageUrl">
            <div class="image-preview-action">
              <i class="el-icon-delete" @click="removeImage"/>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import {Component, Model, Vue} from 'vue-property-decorator'
import {uploadImages} from "@/api/images";

@Component({
  name: 'UploadImage'
})
export default class extends Vue {
  @Model('imageUrl', { type: String }) imageUrl: string = ''

  private async uploadImage(option: any) {
    const { data } =  await uploadImages(option.file)
    this.$emit('imageUrl', data.createImage.content)
  }

  private removeImage() {
    this.$emit('imageUrl', '')
  }
}
</script>

<style lang="scss" scoped>

.upload-container {
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
