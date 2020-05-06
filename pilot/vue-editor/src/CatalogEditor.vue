<template>
    <div class="catalog-editor">
        <el-container>
            <el-main>
                <div class="page-wrapper">
                    <div class="page" id="content">
                        <component v-for="item in items" :is="item"></component>
                    </div>
                </div>
            </el-main>
            <el-footer style="position: fixed;  left: 0;  bottom: 0;">
                <el-button-group>
                    <el-button type="primary" icon="el-icon-picture"  @click="showImageDialog = true"></el-button>
                    <el-button type="primary" icon="el-icon-edit"></el-button>
                </el-button-group>
            </el-footer>
        </el-container>
        <image-dialog :dialogVisible="showImageDialog" @closeDialog="handleCloseImageDialog" @selectImage="handleSelectImage"></image-dialog>
    </div>
</template>
<script lang="ts">
import {Vue, Component, Prop, Emit} from 'vue-property-decorator';
import { DirectiveOptions } from "vue";

import * as directives from './directives'

// @ts-ignore
import drr from './components/drr.vue';
// @ts-ignore
import TextBox from './components/TextBox.vue';

// @ts-ignore
import ImageDialog from "./components/ImageDialog.vue";
import {IImage} from "@/api/types";

// Register global directives
Object.keys(directives).forEach(key => {
    Vue.directive(key, (directives as { [key: string ]: DirectiveOptions })[key])
})

@Component({
    name: 'CatalogEditor',
    components: {
        drr,
        TextBox,
        ImageDialog
    }
})
export default class CatalogEditor extends Vue {
    private showImageDialog = false
    private items: []

    private handleSelectImage(image: IImage) {
        console.log("image : " + image)
        this.handleCloseImageDialog()
    }

    private handleCloseImageDialog(){
        this.showImageDialog = false
    }

}

</script>
<style scoped lang="scss">
    .catalog-editor {
        .page-wrapper {
            width: 21cm;
            min-height: 29.7cm;
            padding: 2cm;
            margin: 1cm auto;
            border-radius: 5px;
            background: white;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }
        .page {
            padding: 1cm;
            height: 256mm;
        }
        @page {
            size: A4 landscape;
            margin: 0;
            /*size: landscape;*/
        }
        @media print {
            .page {
                margin: 0;
                border: initial;
                border-radius: initial;
                width: initial;
                min-height: initial;
                box-shadow: initial;
                background: initial;
                page-break-after: always;
            }
        }

        .image {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
    }

</style>
