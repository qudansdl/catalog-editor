<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-card>
      <q-card-section>

        <q-tabs
          v-model="tab"
          dense
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
      </q-card-section>

      <q-separator />

      <q-card-section class="scroll"  style="min-height: 250px;">
        <q-tab-panels v-model="tab" animated style="height: 100%">
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
              @colorSelected="colorSelected"
              @patternSelected="patternSelected"
            ></component>
          </q-tab-panel>
        </q-tab-panels>
      </q-card-section>

      <q-separator />

      <q-card-actions class="fixed-bottom bg-grey-3">
        <q-btn flat label="적용" @click="apply" :disable="content == null"/>
        <q-btn flat label="잘라내기" @click="showCropp" v-if="tab == 'library' || tab == 'upload'"/>
        <q-btn flat label="닫기" @click="showDialog = false"/>
      </q-card-actions>
    </q-card>
    <cropp-image v-model="cropp" v-on:apply="applyCroppedImage"></cropp-image>
  </q-dialog>
</template>
<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import { cloneDeep } from 'lodash';
import upload from './background/upload.vue';
import library from './background/library.vue';
import bgcolor from './background/color.vue';
import bgpattern from './background/pattern.vue';

import CroppImage from './Cropp.vue';

@Component({
  components: {
    upload,
    library,
    bgcolor,
    bgpattern,
    CroppImage,
  },
})
export default class EditBackground extends Vue {
  @Prop({ required: true }) private value!: any;

  private maximizedToggle = true;

  private tab = 'library';

  private cropp = {
    show: false,
    image: '',
  };

  private eventName = 'applyBackground';

  components = [
    { name: 'library', label: '선택' },
    { name: 'upload', label: '업로드' },
    { name: 'bgcolor', label: '색상' },
    { name: 'bgpattern', label: '패턴' },
  ];

  apply() {
    this.$emit(this.eventName, this.content);
    this.showDialog = false;
  }

  applyCroppedImage(cropedImage: any) {
    this.$emit(this.eventName, cropedImage.src);
    this.cropp.show = false;
    this.showDialog = false;
  }

  showCropp() {
    this.cropp = {
      show: true,
      image: this.content,
    };
  }

  colorSelected(color: string) {
    this.eventName = 'applyBackgroundColor';
    this.content = color;
  }

  patternSelected(img: string) {
    this.eventName = 'applyBackgroundPattern';
    this.content = img;
  }

  imageSelected(img: any) {
    this.eventName = 'applyBackground';
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
