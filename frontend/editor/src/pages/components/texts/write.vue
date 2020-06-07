<template>
  <q-card>
    <q-card-section class="row justify-center q-my-md">
      <quill-editor
        ref="quillEditor"
        :content="content"
        @onQuillBack='onQuillBack'
        @onEditorReady='onEditorReady'
        @change="onEditorChange($event)"
      />
    </q-card-section>
  </q-card>
</template>

<script>
import { Component, Prop, Vue } from 'vue-property-decorator';
import { cloneDeep } from 'lodash';

@Component
export default class WriteText extends Vue {
  @Prop({ required: true }) content;

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
