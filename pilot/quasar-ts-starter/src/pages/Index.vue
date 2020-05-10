<template>
  <q-layout view="lHh Lpr lFf">
    <q-drawer
      v-model="showMenu"
      show-if-above
      bordered
      content-class="bg-grey-1"
      side="right"
    >
      <q-layout view="lhr lpr lfr">

        <q-header elevated height-hint="98" class="bg-white text-primary">
          <q-list>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="글자" icon="text_fields" @click="text.show = true"/>
            </q-item>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="그림" icon="image_search"/>
            </q-item>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="배경" icon="grid_on"/>
            </q-item>
          </q-list>
        </q-header>

        <q-footer elevated class="bg-white text-primary">
          <q-list>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Undo" icon="undo"/>
            </q-item>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Redo" icon="redo"/>
            </q-item>
            <q-item v-ripple>
              <q-btn flat :disable="!showDelete"  color="primary" label="Delete" icon="delete" @click="deleteSelected()"/>
            </q-item>
            <q-separator dark inset  color="orange" />
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Save" icon="save_alt" />
            </q-item>
            <q-separator dark inset  color="orange" />
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Template" icon="file_copy" />
            </q-item>
            <q-item clickable v-ripple>
              <q-btn
                flat
                color="primary"
                icon="menu"
                label="Hide Menu"
                @click="showMenu  = !showMenu"
              />
            </q-item>
          </q-list>
        </q-footer>

      </q-layout>

    </q-drawer>

    <q-page-container>
      <div
        class="printing-body absolute-center"
        ref="printMe"
        :style="[{'background': configuration.backgroundPattern ? `url(${configuration.backgroundPattern}) repeat` : `${configuration.backgroundColor || '#ffffff'} url(${configuration.backgroundImg}) 0 0/cover no-repeat`}]">

        <template v-for="(item) in configuration.items">
          <dr
            :key="item.id"
            :item="item"
            @coordinate="onCoordinatesChanged"
            @select="onSelected"
            @deselect="onDeselected">
          </dr>
        </template>
      </div>

      <q-page-sticky position="bottom-right" :offset="[18, 18]" v-if="showMenu == false">
        <q-btn fab icon="menu" color="accent" @click="showMenu = true"/>
      </q-page-sticky>
    </q-page-container>

    <edit-text v-model="text" v-on:apply="addElement"></edit-text>
  </q-layout>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

import { v4 as uuidv4 } from 'uuid';
import { cloneDeep } from 'lodash';
import html2canvas from 'html2canvas';

import VueDraggableResizable from 'vue-draggable-resizable';
import { Debounce } from 'vue-debounce-decorator';

// optionally import default styles
import 'vue-draggable-resizable/dist/VueDraggableResizable.css';

import { Item, Configuration } from '@/types/types';

import SearchTab from '@/components/search.vue';
import ImagesTab from '@/components/images.vue';
import FontTab from '@/components/font.vue';
import TemplatesTab from '@/components/templates.vue';

import EditText from './components/Text.vue';

import dr from './components/DrrWrap.vue';

@Component({
  components: {
    SearchTab,
    EditText,
    ImagesTab,
    FontTab,
    TemplatesTab,
    VueDraggableResizable,
    dr,
  },
})
export default class Index extends Vue {
  text = {
    show: false,
    content: '<p>여기에 내용을 입력하세요</p>',
  };

  showDelete = false;

  showMenu = false;

  countChange = 0;

  configurationHistory = [] as Configuration[];

  configuration: Configuration = {
    backgroundColor: '',
    backgroundImg: '',
    backgroundPattern: '',
    items: [],
  };

  private setShowDelete() {
    this.$nextTick(function () {
      this.showDelete = document.querySelectorAll('.drr.active').length > 0;
      console.log(`showDelete :  + ${this.showDelete}`);
    });
  }

  public mounted(): void {
    const configuration = localStorage.getItem('configuration');
    if (configuration) {
      this.configuration = JSON.parse(configuration);
    }
  }

  async print() {
    const el = this.$refs.printMe;
    await html2canvas(el as HTMLElement).then(
      (canvas) => {
        console.log('canvas', canvas);
        console.log('dataUrl', canvas.toDataURL());

        const link = document.createElement('a');
        link.href = canvas.toDataURL();
        link.download = 'image.jpg';
        document.body.appendChild(link);
        link.click();
      },
    );
  }

  addElement(newItem: Item) {
    const item: Item = {
      id: uuidv4().toUpperCase(),
      x: 100,
      y: 100,
      w: 100,
      h: 100,
      angle: 0,
      src: newItem.src,
      type: newItem.type,
    };

    this.configuration.items.push(item);
    this.updateLocalStorage();
  }

  @Debounce(300)
  updateLocalStorage() {
    const { configuration } = this;
    localStorage.setItem('configuration', JSON.stringify(configuration));

    const clone: Configuration = cloneDeep(configuration);
    console.log('old config before', clone);
    this.configurationHistory.unshift(clone);
    console.log(this.configurationHistory);
  }

  onCoordinatesChanged(newItem: Item) {
    this.configuration.items = this.configuration.items.map((item) => (item.id === newItem.id ? newItem : item));
    this.updateLocalStorage();
  }

  onSelected() {
    this.setShowDelete();
  }

  onDeselected() {
    this.setShowDelete();
  }

  // eslint-disable-next-line class-methods-use-this
  deleteSelected() {
    debugger;
    const activeList = document.querySelectorAll('.drr.active');
    if (activeList.length > 0) {
      this.$q.dialog({
        title: '삭제',
        message: '선택된 내용을 삭제하시겠습니까?',
        cancel: true,
        persistent: true,
      })
        .onOk(() => {
          // console.log('>>>> OK')
        })
        .onOk(() => {
          // console.log('>>>> second OK catcher')
        })
        .onCancel(() => {
          // console.log('>>>> Cancel')
        })
        .onDismiss(() => {
          // console.log('I am triggered on both OK and Cancel')
        });
    }
    // getAttribute

    // this.setShowDelete();
  }

  undo() {
    this.countChange += 1;
    this.configuration = cloneDeep(this.configurationHistory[this.countChange]);
    console.log(this.configurationHistory[this.countChange]);

    console.log('back change', this.configurationHistory[this.countChange]);
    this.updateLocalStorage();
  }

  redo() {
    this.countChange -= 1;
    this.configuration = this.configurationHistory[this.countChange];
    console.log('front change', this.configurationHistory[this.countChange]);
    this.updateLocalStorage();
  }
}

</script>

<style>
  *{
    user-select: none;
  }
  .printing-body {
    position: absolute;
    width: 800px;
    height: 500px;
    border: 8px solid #ccc;
    overflow: hidden;
  }
  .continue_button a{
    width: 100%;
    font-size: 20px;
    background: transparent;
    display: inline-block;
    line-height: 38px;
    text-transform: uppercase;
    font-weight: bold;
    color: #fff;
    padding: 0 20px;
    -webkit-transition: all .5s ease-out;
    transition: all .5s ease-out;
  }
</style>
