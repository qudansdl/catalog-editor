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
              <q-btn flat color="primary" label="그림" icon="image_search" @click="image.show = true"/>
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
        :style="[{'background': status.backgroundPattern ? `url(${status.backgroundPattern}) repeat` : `${status.backgroundColor || '#ffffff'} url(${status.backgroundImg}) 0 0/cover no-repeat`}]">

        <template v-for="(item) in status.items">
          <dw
            :key="item.id"
            :item="item"
            @coordinate="onCoordinatesChanged"
            @select="onSelected"
            @deselect="onDeselected">
          </dw>
        </template>
      </div>

      <q-page-sticky position="bottom-right" :offset="[18, 18]" v-if="showMenu == false">
        <q-btn fab icon="menu" color="accent" @click="showMenu = true"/>
      </q-page-sticky>
    </q-page-container>

    <edit-text v-model="text" v-on:apply="addElement"></edit-text>
    <edit-image v-model="image" v-on:apply="addElement"></edit-image>
  </q-layout>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

import { v4 as uuidv4 } from 'uuid';
import { cloneDeep } from 'lodash';
import html2canvas from 'html2canvas';

import VueDraggableResizable from 'vue-draggable-resizable';
import { Debounce } from 'vue-debounce-decorator';

// optionally import default styles
import 'vue-draggable-resizable/dist/VueDraggableResizable.css';

import { Item, Configuration } from '@/types/types';

import SearchTab from '@/components/search.vue';
import FontTab from '@/components/font.vue';
import TemplatesTab from '@/components/templates.vue';

import { State } from 'vuex-class';
import EditText from './components/EditText.vue';
import EditImage from './components/EditImage.vue';

import dw from './components/DrrWrap.vue';

@Component({
  components: {
    SearchTab,
    EditText,
    EditImage,
    FontTab,
    TemplatesTab,
    VueDraggableResizable,
    dw,
  },
})
export default class Index extends Vue {
  text = {
    show: false,
    content: '<p>여기에 내용을 입력하세요</p>',
  };

  image = {
    show: false,
    content: '',
  };

  showDelete = false;

  showMenu = false;

  countChange = 0;

  @State('status', { namespace: 'AppStatus' })
  public status!: Configuration;

  @State('history', { namespace: 'AppStatus' })
  public history!: Configuration[];

  private setShowDelete() {
    this.$nextTick(function () {
      this.showDelete = document.querySelectorAll('.drr.active').length > 0;
      console.log(`showDelete :  + ${this.showDelete}`);
    });
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

    this.status.items.push(item);
    this.addHistory();
  }

  @Debounce(300)
  addHistory() {
    const clone: Configuration = cloneDeep(this.status);
    console.log('old config before', clone);
    this.history.unshift(clone);
    console.log(this.history);
  }

  onCoordinatesChanged(newItem: Item) {
    this.status.items = this.status.items.map((item) => (item.id === newItem.id ? newItem : item));
    this.addHistory();
  }

  onSelected() {
    this.setShowDelete();
  }

  onDeselected() {
    this.setShowDelete();
  }

  // eslint-disable-next-line class-methods-use-this
  deleteSelected() {
    const activeList = document.querySelectorAll('.drr.active');
    if (activeList.length > 0) {
      this.$q.dialog({
        title: '삭제',
        message: '선택된 내용을 삭제하시겠습니까?',
        cancel: true,
        persistent: true,
      })
        .onOk(() => {
          activeList.forEach((elem, index) => {
            const id = elem.getAttribute('id');
            console.log(`id = ${id}`);
            this.status.items = this.status.items.filter((i) => i.id !== id);
          });
        });
    }
  }

  undo() {
    this.countChange += 1;
    this.status = cloneDeep(this.history[this.countChange]);
    console.log(this.history[this.countChange]);

    console.log('back change', this.history[this.countChange]);
    this.addHistory();
  }

  redo() {
    this.countChange -= 1;
    this.status = this.history[this.countChange];
    console.log('front change', this.history[this.countChange]);
    this.addHistory();
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
