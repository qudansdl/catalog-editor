<template>
        <c-el-dialog
                :visible="dialogVisible"
                :showClose="false"
                @onDialogDrag="handleDialogDrag"
                @close="closeDialog"
                :width="'435px'"
        >
            <el-tabs
                    v-model="activeName"
                    style="margin-top:15px"
                    type="border-card"
            >
                <el-tab-pane
                        key="UPLOAD"
                        label="업로드"
                        name="UPLOAD"
                >
                    <keep-alive>
                        <upload-image v-model="imageURL" />
                    </keep-alive>
                </el-tab-pane>
                <el-tab-pane
                        key="SELECT"
                        label="선택"
                        name="SELECT"
                >
                    <keep-alive>
                        <select-image v-model="imageURL" />
                    </keep-alive>
                </el-tab-pane>
            </el-tabs>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" icon="el-icon-check">선택</el-button>
                <el-button icon="el-icon-close" @click="closeDialog">닫기</el-button>
          </span>
        </c-el-dialog>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator'

    import CElDialog from '../components/dialog'

    import UploadImage from './image/upload.vue'
    import SelectImage from './image/select.vue'

    @Component({
        components: {
            UploadImage,
            SelectImage,
            CElDialog
        }
    })
    export default class ImageDialog extends Vue {
        @Prop() private dialogVisible!: boolean;

        private imageURL: string = '';

        private activeName: string = 'UPLOAD';


        private closeDialog(){
            this.$emit('closeDialog');
        }

        private handleDialogDrag() {
        }
    }
</script>
