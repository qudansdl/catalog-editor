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
        ></component>
      </q-tab-panel>
    </q-tab-panels>
  </q-card>
  </q-dialog>
</template>
<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import { cloneDeep } from 'lodash';
import pc from '../../components/img-bar/img-pc.vue';
import lib from '../../components/img-bar/img-lib.vue';
import url from '../../components/img-bar/img-url.vue';

@Component({
  components: {
    pc,
    lib,
    url,
  },
})
export default class EditImage extends Vue {
  @Prop({ required: true }) private value!: any;

  private maximizedToggle = true;

  private tab = 'lib';

  components = [
    { name: 'lib', label: '선택' },
    { name: 'pc', label: '업로드' },
    { name: 'url', label: 'URL' },
  ];

  // eslint-disable-next-line class-methods-use-this
  mounted() {
  }

  apply() {
    this.$emit('apply', { src: this.value.content, type: 'image' });
    this.showDialog = false;
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
