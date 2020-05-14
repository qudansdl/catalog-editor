<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-card>
      <tinymce
          v-if="tinymceActive"
          ref="editor"
          v-model="content"
          :menubar="false"
      />
      <q-card-actions align="right">
        <q-space />
        <q-btn color="primary" label="적용" @click="apply"/>
        <q-btn color="brown-5" label="닫기" @click="showDialog = false"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import Tinymce from '@/components/Tinymce/index.vue';
import { cloneDeep } from 'lodash';

@Component({
  components: {
    Tinymce,
  },
})
export default class EditText extends Vue {
  @Prop({ required: true }) private value!: any;

  private tinymceActive = false;

  private maximizedToggle = true;

  apply() {
    this.$emit('apply', this.value.item);
    this.showDialog = false;
  }

  deactivated() {
    this.tinymceActive = false;
  }

  activated() {
    this.tinymceActive = true;
  }

  get content() {
    return this.value.item.src;
  }

  set content(content) {
    const newValue = cloneDeep(this.value);
    newValue.item.src = content;

    this.$emit('input', newValue);
  }

  get showDialog() {
    return this.value.show;
  }

  set showDialog(show) {
    const newValue = cloneDeep(this.value);
    newValue.show = show;

    this.$emit('input', newValue);
  }
}
</script>
