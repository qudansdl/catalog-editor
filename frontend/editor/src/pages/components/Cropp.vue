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
      <vue-cropper
        ref="cropper"
        :src="image"
        alt="Source Image"
      >
      </vue-cropper>
    </q-card-section>
    <q-card-actions align="right">
      <q-space />
      <q-btn color="primary" label="적용" @click="cropImage"/>
      <q-btn color="brown-5" label="닫기" @click="showDialog = false"/>
    </q-card-actions>
  </q-card>
  </q-dialog>
</template>
<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';

import VueCropper from 'vue-cropperjs';
import 'cropperjs/dist/cropper.css';
import { cloneDeep } from 'lodash';


@Component({
  components: {
    VueCropper,
  },
})
export default class CroppImage extends Vue {
  @Prop({ required: true }) private value!: string;


  cropImage() {
    const cropImg = this.$refs.cropper.getCroppedCanvas().toDataURL();
    this.$emit('apply', { src: cropImg, type: 'image' });
    this.showDialog = false;
  }

  get image() {
    return this.value.image;
  }

  set image(image) {
    const newValue = cloneDeep(this.value);
    newValue.image = image;

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
