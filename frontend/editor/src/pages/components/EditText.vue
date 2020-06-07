<template>
  <q-dialog
    v-model="showDialog"
    persistent
    :maximized="maximizedToggle"
    transition-show="slide-up"
    transition-hide="slide-down"
  >
    <q-card>
      <q-card-section >
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
              :content="content"
              @textSelected="textSelected"
            ></component>
          </q-tab-panel>
        </q-tab-panels>
      </q-card-section>

      <q-separator />

      <q-card-actions class="fixed-bottom bg-grey-3">
        <q-btn flat label="적용" @click="apply" :disable="content == null"/>
        <q-btn flat label="닫기" @click="showDialog = false"/>
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

  private tab = 'write';

  components = [
    { name: 'write', label: '작성' },
    { name: 'library', label: '선택' },
  ];

  mounted(){
   this.tab = 'write'

  }
  apply() {
    this.$emit('apply', this.value.item);
    this.content = null
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

  textSelected(text: any) {
    this.content = text;
  }
}
</script>
