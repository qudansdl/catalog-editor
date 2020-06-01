<template>
  <div class="q-pa-md">
    <q-card>
      <div class="row q-col-gutter-xs">
        <div class="col">
          <q-card-section>
            <quill-editor
              :content="content"
              @change="onEditorChange($event)"
            />
          </q-card-section>
        </div>

      </div>
    </q-card>
  </div>
</template>

<script>

import { Component, Vue } from 'vue-property-decorator';

@Component
export default class WriteText extends Vue {
  content = '';

  onEditorChange(quill, html, text) {
    console.log('editor change!', quill, html, text)
    this.$emit('textSelected', html);
  }
};
</script>
