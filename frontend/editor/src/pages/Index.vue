<template>
  <q-layout view="lHh Lpr lFf">
        <q-footer reveal elevated class="bg-white text-primary" v-if="showMenu == true">
          <q-toolbar>
            <div class="q-pa-md">
            <div class="row">
              <div class="col">
                <q-btn flat color="primary" icon="text_fields" @click="showEditText"/>
              </div>
              <div class="col">

                <q-btn flat color="primary" icon="image_search" @click="image.show = true"/>
              </div>
              <div class="col-auto">
                <q-btn flat color="primary" icon="grid_on" @click="background.show = true"/>
              </div>
              <div class="col">
              <q-btn flat :disable="!showDelete" color="primary" icon="flip_to_front" @click="flipToFront"/>
              </div>
              <div class="col">
                <q-btn flat :disable="!showDelete" color="primary" icon="flip_to_back" @click="flipToBack"/>
              </div>
              <div class="col">
              <q-btn flat :disable="!showDelete"  color="primary" icon="delete" @click="deleteSelected()"/>
              </div>
            </div>
            <div class="row">
              <div class="col">

              <q-btn flat color="primary" icon="undo" @click="undo" :disable="changeIndex == 0"/>
              </div>
              <div class="col">
              <q-btn flat color="primary" icon="redo" @click="redo" :disable="changeIndex + 1 >= history.length"/>
              </div>


              <div class="col">
              <q-btn flat color="primary" icon="folder_open"  @click="catalog.show = true"/>
              </div>
              <div class="col">
              <q-btn flat color="primary" icon="file_copy"  @click="template.show = true"/>
              </div>

            <div class="col">
              <q-btn flat color="primary" icon="save_alt"  @click="saveCatalog()"/>
            </div>
              <div class="col">
              <q-btn
                flat
                color="primary"
                icon="menu"
                @click="showMenu = !showMenu"
              />
              </div>
            </div>
            </div>
          </q-toolbar>
        </q-footer>
    <q-page-container>
      <div
        class="printing-body absolute-center"
        ref="printMe"
        :style="[{'background': status.backgroundPattern ? `url(${status.backgroundPattern}) repeat` : `${status.backgroundColor || '#ffffff'} url(${status.backgroundImg}) 0 0/cover no-repeat`}]">
          <dw
            v-for="(item) in status.items"
            ref="items"
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

      <q-page-sticky position="bottom-right" :offset="[18, 18]" v-if="showMenu == false" class="no-print">
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
    <edit-template v-model="template" v-on:apply="setTemplate"></edit-template>
    <edit-catalog v-model="catalog" v-on:apply="setCatalog"></edit-catalog>

    <span v-if="catalogLoaded" id="catalogLoaded"></span>
    <q-circular-progress
      v-if="isLoading"
      indeterminate
      size="150px"
      :thickness="0.2"
      color="lime"
      center-color="grey-8"
      track-color="transparent"
      class="q-ma-md absolute-center"
    />
  </q-layout>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

import { v4 as uuidv4 } from 'uuid';


import VueDraggableResizable from 'vue-draggable-resizable';
import { Debounce } from 'vue-debounce-decorator';

import 'vue-draggable-resizable/dist/VueDraggableResizable.css';

import { Item, Configuration } from '@/types/types';

import { Mutation, State } from 'vuex-class';
import EditBackground from 'pages/components/EditBackground.vue';
import EditText from './components/EditText.vue';
import EditImage from './components/EditImage.vue';
import EditTemplate from './components/EditTemplate.vue';
import EditCatalog from './components/EditCatalog.vue';
import dw from './components/DrrWrap.vue';
import ApiCatalog from '@/api/catalogs';
import { ITemplateData, ICatalogData } from '@/api/types';
import ApiTemplate from '@/api/templates';
import { cloneDeep } from 'lodash';

@Component({
  components: {
    EditBackground,
    EditText,
    EditImage,
    EditTemplate,
    EditCatalog,
    VueDraggableResizable,
    dw,
  },
})
export default class Index extends Vue {
  isLoading = false
  catalogLoaded = false

  type = 'CATALOG'

  template = {
    id: null as string|null,
    show: false
  };

  catalog = {
    id: null as string|null,
    show: false
  };

  entity: ICatalogData | ITemplateData | null = null

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

  @Mutation('SET_HISTORY', { namespace: 'AppStatus' })
  public updateHistiory!: any;

  @State('capture', { namespace: 'AppStatus' })
  public capture!: boolean;

  @Mutation('SET_CAPTURE', { namespace: 'AppStatus' })
  public updateCapture!: any;

  async mounted() {
    const type = this.$route.query.type as string
    const id = this.$route.query.id as string
    if(type)
    {
      if(id) {
        let content = ''
        if (this.type === 'CATALOG') {
          const catalog = await ApiCatalog.getCatalog(id)
          content = catalog.content
          this.entity = catalog
        } else if (this.type === 'TEMPLATE') {
          this.template.id = id
          const template = await ApiTemplate.getTemplate(id)
          content = template.content
          this.entity = template
        }
        this.updateStatus(JSON.parse(content))
      }
    }
    this.history.push(cloneDeep(this.status));
  }

  updated() {
    console.log('updated called')
    this.catalogLoaded = true
  }

  private setShowDelete() {
    this.$nextTick(function () {
      this.showDelete = document.querySelectorAll('.drr.active').length > 0;
      console.log(`showDelete :  + ${this.showDelete}`);
    });
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
  setTemplate(template: ITemplateData) {
    this.updateStatus(JSON.parse(template.content!))
    this.addHistory();
  }
  setCatalog(catalog: ICatalogData) {
    this.updateStatus(JSON.parse(catalog.content!))
    this.entity = catalog

    this.updateHistiory([])
    this.changeIndex = 0;
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

  onSelected(item: Item) {
    let items = this.$refs.items as Array<any>
    for(let idx = 0; idx < items.length; idx++)
    {
      if(item.id != this.status.items[idx].id)
      {
        items[idx].deselect()
      }
    }

    this.setShowDelete()
  }

  onDeselected() {
    this.setShowDelete()
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


  thumbnailify(base64Image: string, targetWidth: number, targetHeight: number): Promise<string> {
    return new Promise(function(resolve, reject) {
      const img = new Image();
      img.onload = function() {
        const width = img.width,
          height = img.height,
          canvas = document.createElement('canvas'),
          ctx = canvas.getContext('2d')!;

        canvas.width = targetWidth
        canvas.height = targetHeight;

        ctx.drawImage(
          img,
          0,
          0,
          targetWidth,
          targetHeight
        );

        resolve(canvas.toDataURL());
      };

      img.src = base64Image;
    })
  }

  async print() {
    const el = this.$refs.printMe as any;
    const rect = el.getBoundingClientRect()

    const left = (rect.left as number) + 4
    const top = (rect.top as number) + 4
    const bottom = rect.bottom - 8
    const right = rect.right - 8

    return await (this as any).$html2canvas(el, {
      type: 'dataURL',
      removeContainer: true,
      width: right - left,
      height: bottom - top,
      x: left,
      y: top
    })
  }


  async captureAndSave(name:string) {
    this.isLoading = true
      const image = await this.print()
      const thumbnail = await this.thumbnailify(image, 150, 100)

      if(this.type === 'TEMPLATE')
      {
        if(this.entity)
        {
          const { data } = await ApiTemplate.updateTemplate(
            this.entity.id!,
            name,
            JSON.stringify(this.status),
            image,
            thumbnail,
            [])
        }else {
          const { data } = await ApiTemplate.createTemplate(
            name,
            JSON.stringify(this.status),
            image,
            thumbnail,
            [])
          this.entity = data.createCatalog
        }
      }else
      {
        if(this.entity)
        {
          const { data } = await ApiCatalog.updateCatalog(
            this.entity.id!,
            name,
            JSON.stringify(this.status),
            image,
            thumbnail,
            [])
        }else {
          const { data } = await ApiCatalog.createCatalog(
            name,
            JSON.stringify(this.status),
            image,
            thumbnail,
            [])
          this.entity = data.createCatalog
        }
      }

    this.$q.dialog({
      title: '저장',
      message: '저장했습니다'
    }).onOk(() => {
      window.parent.postMessage('contentsaved', '*');
    })
    this.updateCapture(false)
    this.isLoading = false
  }

  saveCatalog() {
    this.$q.dialog({
      title: '확인',
      message: '카탈로그 이름을 입력하세요',
      prompt: {
        model: this.entity ? this.entity.name : (this as any).$moment().format('YYYY년 MM월 DD'),
        type: 'text' // optional
      },
      cancel: true,
      persistent: true
    }).onOk( (name: any) => {
      this.updateCapture(true)
      this.$nextTick(()  => {
         this.captureAndSave(name);
      });
    })
  }

  flipToBack(){
    const activeList = document.querySelectorAll('.drr.active');

    activeList.forEach((elem, index) => {
      const id = elem.getAttribute('id');
      console.log(`id = ${id}`);

      const items = this.status.items;
      const idx = items.findIndex(i => i.id == id);
      items.unshift (items.splice(idx, 1)[0]);
    });
  }

  flipToFront(){
    const activeList = document.querySelectorAll('.drr.active');

    activeList.forEach((elem, index) => {
      const id = elem.getAttribute('id');
      console.log(`id = ${id}`);

      const items = this.status.items;
      const idx = items.findIndex(i => i.id == id);
      items.push(items.splice(idx, 1)[0]);
    });
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
<style lang="sass">
  .selected-item
    background-color: $teal-1
</style>
