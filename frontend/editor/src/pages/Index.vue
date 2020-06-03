<template>
  <q-layout view="lHh Lpr lFf">
    <q-drawer
      v-model="showMenu"
      show-if-above
      bordered
      content-class="bg-grey-1"
      side="right"
      class="right-menu"
    >
      <q-layout view="lhr lpr lfr">

        <q-header elevated height-hint="98" class="bg-white text-primary">
          <q-list>
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat color="primary" icon="text_fields" @click="showEditText"/>
            </q-item>
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat color="primary" icon="image_search" @click="image.show = true"/>
            </q-item>
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat color="primary" icon="grid_on" @click="background.show = true"/>
            </q-item>
          </q-list>
        </q-header>

        <q-footer elevated class="bg-white text-primary">
          <q-list>
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat color="primary" icon="undo" @click="undo" :disable="changeIndex == 0"/>
            </q-item>
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat color="primary" icon="redo" @click="redo" :disable="changeIndex + 1 >= history.length"/>
            </q-item>
            <q-item v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat :disable="!showDelete"  color="primary" icon="delete" @click="deleteSelected()"/>
            </q-item>
            <q-separator dark inset  color="orange" />
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat color="primary" icon="save_alt" />
            </q-item>
            <q-separator dark inset  color="orange" />
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn flat color="primary" icon="file_copy" />
            </q-item>
            <q-item clickable v-ripple style="padding: 0px;min-height: 20px;">
              <q-btn
                flat
                color="primary"
                icon="menu"
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
aaaaaaaaa
          <dw
            v-for="(item) in status.items" ref="items"
            :key="item.id"
            :item="item"
            @coordinate="onCoordinatesChanged"
            @select="onSelected"
            @deselect="onDeselected"
            @delete="onDelete"
            @content-active="onContentActive">
          </dw>
        <q-resize-observer @resize="onResize"></q-resize-observer>
      </div>

      <q-page-sticky position="bottom-right" :offset="[18, 18]" v-if="showMenu == false">
        <q-btn fab icon="menu" color="accent" @click="showMenu = true"/>
      </q-page-sticky>
    </q-page-container>

    <edit-text v-model="text" v-on:apply="setText"></edit-text>
    <edit-image v-model="image" v-on:apply="addElement"></edit-image>
    <edit-background
      v-model="background"
      @applyBackground="applyBackground"
      @applyBackgroundColor="applyBackgroundColor"
      @applyBackgroundPattern="applyBackgroundPattern"
    ></edit-background>
  </q-layout>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

import { v4 as uuidv4 } from 'uuid';
import { cloneDeep } from 'lodash';
import html2canvas from 'html2canvas';

import VueDraggableResizable from 'vue-draggable-resizable';
import { Debounce } from 'vue-debounce-decorator';

import 'vue-draggable-resizable/dist/VueDraggableResizable.css';

import { Item, Configuration } from '@/types/types';

import { Mutation, State } from 'vuex-class';
import EditBackground from 'pages/components/EditBackground.vue';
import EditText from './components/EditText.vue';
import EditImage from './components/EditImage.vue';
import dw from './components/DrrWrap.vue';

@Component({
  components: {
    EditBackground,
    EditText,
    EditImage,
    VueDraggableResizable,
    dw,
  },
})
export default class Index extends Vue {
  text = {
    show: false,
    item: {
      src: '<p>여기에 내용을 입력하세요</p>',
      type: 'text',
    },
  };

  image = {
    show: false,
    content: '',
  };

  background = {
    show: false,
    content: '',
  };

  showDelete = false;

  showMenu = false;

  changeIndex = 0;

  contentSize = {width: 0, height: 0}

  @State('status', { namespace: 'AppStatus' })
  public status!: Configuration;

  @Mutation('SET_STATUS', { namespace: 'AppStatus' })
  public updateStatus!: any;

  @State('history', { namespace: 'AppStatus' })
  public history!: Configuration[];

  mounted() {
    if(this.$route.query.catalogId)
    {

    }
    this.history.push(cloneDeep(this.status));
  }

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

  showEditText() {
    this.text = {
      show: true,
      item: {
        src: '<p>여기에 내용을 입력하세요</p>',
        type: 'text',
      },
    };
  }

  setText(newItem: Item) {
    if (newItem.id) {
      const idx = this.status.items.findIndex((i) => i.id === newItem.id);
      this.status.items[idx] = newItem;
      (this.$refs.items as [any])[idx].$emit('content-inactive');
    } else {
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
    }

    this.addHistory();
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

  applyBackground(img: string) {
    this.status.backgroundImg = img;

    this.status.backgroundPattern = '';
    this.status.backgroundColor = '';
    this.addHistory();
  }

  applyBackgroundColor(color: string) {
    this.status.backgroundColor = color;

    this.status.backgroundPattern = '';
    this.status.backgroundImg = '';

    this.addHistory();
  }

  applyBackgroundPattern(img: string) {
    this.status.backgroundPattern = img;
    this.status.backgroundColor = '';
    this.status.backgroundImg = '';

    this.addHistory();
  }

  @Debounce(300)
  addHistory() {
    if (this.changeIndex < this.history.length - 1) {
      this.history.length = this.changeIndex + 1;
    }
    const clone: Configuration = cloneDeep(this.status);
    this.history.push(clone);
    this.changeIndex += 1;
    console.log('history index', this.changeIndex, this.history.length);
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

  onDelete(item: Item) {
    const idx = this.status.items.findIndex((i) => i.id === item.id);
    this.status.items.splice(idx, 1);
    this.addHistory();
  }

  onContentActive(item: Item) {
    this.text.item = item;
    this.text.show = true;
  }

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
    this.changeIndex -= 1;

    const newStatus = cloneDeep(this.history[this.changeIndex]);
    this.updateStatus(newStatus);

    console.log('back change', this.changeIndex, this.history.length);
  }

  redo() {
    this.changeIndex += 1;
    const newStatus = this.history[this.changeIndex];
    this.updateStatus(newStatus);

    console.log('front change', this.changeIndex, this.history.length);
  }

  onResize (size: any) {
    this.contentSize = size
  }
}

</script>

<style>
  *{
    user-select: none;
  }
  .printing-body {
    position: absolute;
    height:100%;
    width:100%;
    border: 8px solid #ccc;
    overflow: hidden;
  }
  .right-menu {
    width: calc(100% - 80px);
  }
  button {
    background: none;
    border: none;
    cursor: pointer;
    margin: auto;
    display: block;
  }
  .image {
    float: left;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
    border: 1px solid #ebebeb;
    margin: 5px;
  }
  .vue-select-image__item {
    margin-left: 0px !important;
  }
</style>
