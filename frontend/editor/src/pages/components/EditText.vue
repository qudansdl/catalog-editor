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
            @textSelected="textSelected"
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
import write from './texts/write.vue';
import library from './texts/library.vue';

@Component({
  components: {
    write,
    library,
  },
})
export default class EditText extends Vue {
  @Prop({ required: true }) private value!: any;

  private maximizedToggle = true;


  private tab = 'library';

  components = [
    { name: 'library', label: '선택' },
    { name: 'write', label: '작성' },
  ];

  apply() {
    this.$emit('apply', this.value.item);
    this.showDialog = false;
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

  onEditorChange(quill: any, html: any, text: any) {
    console.log('editor change!', quill, html, text)
    this.content = html
  }

  textSelected(text: any) {
    this.content = text;
  }
}
</script>
