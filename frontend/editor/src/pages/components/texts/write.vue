<template>
  <div class="q-pa-md">
      <div class="row q-col-gutter-xs">
        <div class="col">
            <quill-editor
              ref="quillEditor"
              :content="content"
              @onQuillBack='onQuillBack'
              @onEditorReady='onEditorReady'
              @change="onEditorChange($event)"
            />
        </div>

      </div>
  </div>
</template>

<script>
import { Component, Prop, Vue } from 'vue-property-decorator';
import { cloneDeep } from 'lodash';

@Component
export default class WriteText extends Vue {
  @Prop({ required: true }) content;

  mounted(){
    const editor = this.$refs.quillEditor
    /*
    const delta = {
      ops: [
        { insert:  this.content }
      ]
    }
   //editor.quill.setContents(delta)
     */
  }

  onEditorReady(quill) {
  }

  onQuillBack(quill) {
    quill.deleteText(0, quill.getLength())
  }
  onEditorChange(quill) {
    console.log('editor change!', quill, quill.html, quill.text)
    this.$emit('textSelected', quill.html);
  }
};
</script>

<style>
  .ql-editor {
    min-height:125px;
  }
</style>
