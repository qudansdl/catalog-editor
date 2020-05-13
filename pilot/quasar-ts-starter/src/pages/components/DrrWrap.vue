<template>
    <div
      @mouseup="reSet"
      ref="ddrContRef"
      class="drr-wrap"
      >
      <drr
        :id="item.id"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :angle="item.angle"
        :rotateable="true"
        :draggable="true"

        @rotate="printRect"
        @rotatestop="rotateStop"
        @change="onChange"
        @drag="onDrag"
        @resize="onResize"
        @dragstop="onDragStop"
        @select="onSelected"
        @deselect="onDeselected"
        >
        <button class="close" @click.stop.prevent="deleteItem()">&times;</button>
        <!-- rotate info  -->
        <div ref="ddrInfo" :class="['ddr', {hidden:test}]"></div>
        <template v-if="item.type == 'text'">
          <fitty ref="fitty">
            <template  v-slot:content>
              <div v-html="item.src"></div>
            </template>
          </fitty>
        </template>
        <template v-if="item.type == 'image'">
          <img :src="item.src" alt="" v-if="item.src && item.type == 'image'" style="width: 100%; height: 100%; position: absolute">
        </template>
      </drr>

      <template  v-if="guideLine.active">
      <span class="hori" :style="{top: guideLine.posTop + 'px'}"></span>
      <span class="hori" :style="{top: guideLine.posBottom + 'px'}"></span>
      <span class="vert-right" :style="{left: guideLine.posRight +'px'}"></span>
      <span class="vert-left" :style="{left: guideLine.posLeft +'px'}"></span>
      </template>
    </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import { cloneDeep } from 'lodash';
import { Item } from '@/types/types';
import Fitty from 'components/vue-fitty/Fitty.vue';
import { FittyInstance } from 'fitty';
import { Debounce } from 'vue-debounce-decorator';
import drr from './drr.vue';

interface Rect {
  x: number;
  y: number;
  w: number;
  h: number;
  angle: number;
}

@Component({
  components: {
    Fitty,
    drr,
  },
})
export default class Drr extends Vue {
  @Prop() readonly item: Item | undefined;

  selected = false;

  test = true;

  guideLine = {
    active: false,
    posTop: 0,
    posBottom: 0,
    posRight: 0,
    posLeft: 0,
  };

  public mounted(): void {
    document.addEventListener('keydown', (e: KeyboardEvent) => {
      const refContent = this.$refs.ddrContRef as Element;
      if (
        refContent
        && refContent.children
        && refContent.children.length
        && refContent.children[0].classList.contains('active')) {
        this.moveFunc(e);
      }
    });
  }


  moveFunc(e: KeyboardEvent) {
    const newItem = cloneDeep(this.item) as Item;
    if (e.key === 'ArrowUp') {
      newItem.y -= 1;
    } else if (e.key === 'ArrowDown') {
      newItem.y += 1;
    } else if (e.key === 'ArrowRight') {
      newItem.x += 1;
    } else if (e.key === 'ArrowLeft') {
      newItem.x -= 1;
    }

    this.$emit('coordinate', newItem);
    e.preventDefault();
  }

  printRect(rect: Rect) {
    const ddrInfo = this.$refs.ddrInfo as Element;
    const r = rect.angle;
    ddrInfo.innerHTML = (r < 0 ? Math.floor(360 + r) : Math.round(rect.angle)).toString();
    this.test = false;
  }

  rotateStop() {
    setTimeout(() => {
      this.test = true;
    }, 500);
  }

  @Debounce(600)
  redrawText() {
    const fitty: FittyInstance = this.$refs.fitty as any;
    console.log('fit text');
    if (fitty) fitty.fit();
  }

  onDragStop() {
    this.redrawText();
  }

  onSelected() {
    this.$emit('select', this.item);
  }

  onDeselected() {
    this.$emit('deselect', this.item);
  }

  deleteItem() {
    this.$emit('delete', this.item);
  }

  @Debounce(600)
  reSet() {
    const ddrCont: Element = this.$refs.ddrContRef as any;
    const { guideLine } = this;

    if (ddrCont && ddrCont.children[0]) {
      ddrCont.children[0].className = 'drr active';
    }
    guideLine.active = false;
  }

  onChange(rect: Rect) {
    this.onRectChanged(rect);
  }

  onDrag(rect: Rect) {
    this.onRectChanged(rect);
  }

  onResize(rect: Rect) {
    this.onRectChanged(rect);
  }

  static getRotatedPoint(x: number, y: number, ang: number, isDeg = true) {
    // X = x*cos(θ) - y*sin(θ)
    // Y = x*sin(θ) + y*cos(θ)
    let radian = ang;
    if (isDeg) radian = ang * (Math.PI / 180);

    const sinAng = Math.sin(radian);
    const cosAng = Math.cos(radian);

    const newX = x * cosAng - y * sinAng;
    const newY = x * sinAng + y * cosAng;

    return { x: newX, y: newY };
  }

  onRectChanged(rect: Rect) {
    const item = cloneDeep(this.item) as Item;
    item.x = rect.x;
    item.y = rect.y;

    this.guideLine.active = false;

    item.w = rect.w;
    item.h = rect.h;
    item.angle = rect.angle;

    const topLeft = Drr.getRotatedPoint(rect.x - rect.w / 2, rect.y + rect.h / 2, rect.angle);
    const topRight = Drr.getRotatedPoint(rect.x + rect.w / 2, rect.y + rect.h / 2, rect.angle);
    const bottomLeft = Drr.getRotatedPoint(rect.x - rect.w / 2, rect.y - rect.h / 2, rect.angle);
    const bottomRight = Drr.getRotatedPoint(rect.x + rect.w / 2, rect.y - rect.h / 2, rect.angle);

    this.guideLine.posLeft = Math.min(topLeft.x, topRight.x, bottomLeft.x, bottomRight.x);
    this.guideLine.posBottom = Math.min(topLeft.y, topRight.y, bottomLeft.y, bottomRight.y);
    this.guideLine.posRight = Math.max(topLeft.x, topRight.x, bottomLeft.x, bottomRight.x);
    this.guideLine.posTop = Math.max(topLeft.y, topRight.y, bottomLeft.y, bottomRight.y);

    this.$emit('coordinate', item);

    this.redrawText();
  }
}
</script>

<style>
  img{
      padding: 2px;
      width: 100%;
      height: inherit;
  }

  .hori{
    border-top: 1px dashed;
    width: 100%;
    position: absolute;
  }
  .vert-right{
      border-right: 1px dashed;
      height: 100%;
      position: absolute;
  }
  .vert-left{
      border-left: 1px dashed;
      height: 100%;
      position: absolute;
  }


  .drr-wrap .close {
    position: absolute;
    top: 2px;
    right: 2px;
    z-index: 11000;
    background-color: #FFF;
    padding: 5px 2px 2px;
    color: #000;
    font-weight: bold;
    cursor: pointer;
    opacity: .2;
    text-align: center;
    font-size: 22px;
    line-height: 10px;
    border-radius: 50%;
  }
  .drr-wrap:hover .close {
    opacity: 1;
  }
</style>
