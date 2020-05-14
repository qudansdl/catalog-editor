<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
  <q-card>
    <q-tabs
      v-model="tab"
      dense
      class="text-grey"
      active-color="primary"
      indicator-color="primary"
      align="justify"
      narrow-indicator
    >
      <q-tab
        v-for="item of components"
        :key="item.name"
        v-model="tab"
        :name="item.name"
        :label="item.label"/>
    </q-tabs>

    <q-separator />

    <q-tab-panels v-model="tab" animated>
      <q-tab-panel
        v-for="tab in components"
        :key="tab.name"
        :name="tab.name"
        class="not-padding"
      >
        <component
          :is="tab.name"
          :key="tab.name"
          @imageSelected="imageSelected"
        ></component>
      </q-tab-panel>
    </q-tab-panels>

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
import { cloneDeep } from 'lodash';
import upload from './images/upload.vue';
import library from './images/library.vue';
import url from './images/url.vue';

@Component({
  components: {
    upload,
    library,
    url,
  },
})
export default class EditImage extends Vue {
  @Prop({ required: true }) private value!: any;

  private maximizedToggle = true;

  private tab = 'library';

  components = [
    { name: 'library', label: '선택' },
    { name: 'upload', label: '업로드' },
    { name: 'url', label: 'URL' },
  ];

  apply() {
    this.$emit('apply', { src: this.value.content, type: 'image' });
    this.showDialog = false;
  }

  imageSelected(img: any) {
    this.content = img;
  }

  get content() {
    return this.value.content;
  }

  set content(content) {
    const newValue = cloneDeep(this.value);
    newValue.content = content;

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
