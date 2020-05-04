<template>
    <div
      @mouseup="reSet"
      ref="ddrContRef"
      >
      <drr
        :x="x"
        :y="y"
        :w="width"
        :h="height"
        :angle="angle"
        :rotateable="true"
        :draggable="true"

        @rotate="printRect"
        @rotatestop="rotateStop"
        @change="onChange"
        @drag="onDrag"
        @resize="onResize"
        @dragstop="onDragStop"
        >
          <!-- rotate info  -->
          <div ref="ddrInfo" :class="['ddr', {hidden:test}]"></div>
        <template v-if="item.type == 'text'">
          <fitty ref="fitty">
            <template  v-slot:content>
              <slot/>
            </template>
          </fitty>
        </template>
        <img :src="item.src" alt="" v-if="item.src && item.type == 'image'" style="width: 100%; height: 100%; position: absolute">
      </drr>

      <template  v-if="guideLine.active">
      <span class="hori" :style="{top: guideLine.posTop + 'px'}"></span>
      <span class="hori" :style="{top: guideLine.posBottom + 'px'}"></span>
      <span class="vert-right" :style="{left: guideLine.posRight +'px'}"></span>
      <span class="vert-left" :style="{left: guideLine.posLeft +'px'}"></span>
      </template>
    </div>
</template>

<script>
import Fitty from '@/components/vue-fitty/Fitty.vue'

export default {
  mounted() {
    document.addEventListener('keydown', (e) => {
      if(
      this.$refs.ddrContRef &&
      this.$refs.ddrContRef.children &&
      this.$refs.ddrContRef.children.length &&
      this.$refs.ddrContRef.children[0].classList.contains('active')) {
        this.moveFunc(e)
      }
    })
  },
  props: ['items','itemIndex','item'],
  components: {
    Fitty
  },

  data(){
    return {
      width: this.item.w,
      height: this.item.w,
      x: this.item.x,
      y: this.item.y,
      angle: this.item.angle,
      test: true,
      guideLine: {
        active: false,
        posTop: 0,
        posBottom: 0,
        posRight: 0,
        posLeft: 0,
      }
    }
  },

  methods: {
    moveFunc(e){
        if (e.key == 'ArrowUp') {
          this.y -= 1
        }
        else if (e.key == 'ArrowDown'){
          this.y += 1
        }
        else if (e.key == 'ArrowRight'){
          this.x += 1
        }
        else if (e.key == 'ArrowLeft') {
          this.x -= 1
        }

      let item = {x: this.x, y: this.y, w: this.width, h: this.height, angle: this.angle, src:this.item.src, type: this.item.type}
      this.$emit('coordinate', item, this.itemIndex)
      e.preventDefault()
    },

    printRect(rect){
      let r = rect.angle
      this.$refs.ddrInfo.innerHTML = r < 0 ? Math.floor(360 + r) : Math.round(rect.angle)
      this.test=false
    },

    rotateStop(){
      setTimeout(() => {
        this.test = true
      }, 500)
    },

    redrawText: _.debounce(function() {
      console.log('onDragStop')
      if(this.$refs.fitty)
        this.$refs.fitty.fit()
    }, 600),


    onDragStop(){
      this.redrawText()
    },

    reSet: _.debounce(function() {
      let w = document.querySelectorAll('.drr')

      for(let i in w){
        if(w.hasOwnProperty(i)){
          w[i].className = 'drr inactive'
        }
      }

      if(this.$refs.ddrContRef.children[0]) {
          this.$refs.ddrContRef.children[0].className = 'drr active'
      }
      this.guideLine.active = false
    }, 600),
    onChange(rect) {
      this.onRectChanged(rect)
    },
    onDrag(rect) {
      this.onRectChanged(rect)
    },
    onResize(rect) {
      this.onRectChanged(rect)
    },
    getRotatedPoint(x,y, ang, isDeg = true) {
      // X = x*cos(θ) - y*sin(θ)
      // Y = x*sin(θ) + y*cos(θ)
      let radian = ang
      if(isDeg) radian = ang * (Math.PI / 180)

      const sinAng = Math.sin(radian)
      const cosAng = Math.cos(radian)

      let newX = x*cosAng - y*sinAng
      let newY = x*sinAng + y*cosAng

      return {x:newX, y:newY}
    },
    onRectChanged(rect){
      this.x = rect.x
      this.y = rect.y

      this.guideLine.active = false

      this.width  = rect.w
      this.height = rect.h
      this.angle  = rect.angle

      let topLeft = this.getRotatedPoint(rect.x - rect.w/2, rect.y + rect.h/2, rect.angle)
      let topRight = this.getRotatedPoint(rect.x + rect.w/2, rect.y + rect.h/2, rect.angle)
      let bottomLeft = this.getRotatedPoint(rect.x - rect.w/2, rect.y - rect.h/2, rect.angle)
      let bottomRight = this.getRotatedPoint(rect.x + rect.w/2, rect.y - rect.h/2, rect.angle)

      this.guideLine.posLeft = Math.min(topLeft.x, topRight.x, bottomLeft.x, bottomRight.x)
      this.guideLine.posBottom = Math.min(topLeft.y, topRight.y, bottomLeft.y, bottomRight.y)
      this.guideLine.posRight = Math.max(topLeft.x, topRight.x, bottomLeft.x, bottomRight.x)
      this.guideLine.posTop = Math.max(topLeft.y, topRight.y, bottomLeft.y, bottomRight.y)

      let item = {x: this.x, y: this.y, w: this.width, h: this.height, angle: this.angle, src:this.item.src, type: this.item.type}
      this.$emit('coordinate', item, this.itemIndex)

      this.redrawText()
    },

  }
}
</script>

<style>
  img{
      padding: 2px;
      width: 100%;
      height: inherit;
  }
  .ddr {
    position: absolute;
    top: -40px;
    right: 48%;
  }
  .hidden {
    display: none;
  }

  .dot-info  {
    position: absolute;
    padding: 4px;
  }

  .ddr-top {
    top: 0;
    left: 0;
  }

  .ddr-left {
    top: 0;
    right: 0;
  }

  .ddr-bottom {
    bottom: 0;
    right: 0;
  }

  .ddr-right {
    bottom: 0;
    left: 0;
  }

  .ddr-center {
    bottom: 45%;
    top: 45%;
  }

  .d-d {
    width: 100%;
     position: absolute;
     border: 1px dashed;
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
</style>
