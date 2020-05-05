<template>
  <div id="app">
    <div class="scnt">
        <div class="constructor_area">
            <div class="constructor_page_wrapper">
                <div class="constuctor_page__square" >
                    <!-- square lines -->
                    <div class="square__back__lines__cnt">
                        <div class="back__line"></div>
                        <div class="back__line"></div>
                        <div class="back__line"></div>
                        <div class="back__line"></div>
                    </div>
                    <div id="paper" class="constructor__inner__border__cnt">
                        <div
                            class="printing-body"
                            ref="printMe"
                            :style="[{'background': $root.bgPtrn ? `url(${$root.bgPtrn}) repeat` : `${$root.bgColor || '#ffffff'} url(${$root.bgImg}) 0 0/cover no-repeat`}]">

                            <template v-for="(item, idx) in $root.inputsArr.items">
                            <dr
                                :key="item.id"
                                :item="item"
                                @coordinate="onCoordinatesChanged"
                                :items="$root.inputsArr.items"
                                :itemIndex="idx" >
                            </dr>
                            </template>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="side-ctn">
        <div :class="['aside-block', {active: isActive}]"  >
            <div class="aside-active-tab-cnt">
                <component :is="selectedComponent" @addText="pushElement" :lalala="output"></component>
            </div>


            <div class="left_border">
                <button class="toggle-btn" @click="toggleAside">
                    <i :class="[`fas fa-chevron-${!this.isActive ? 'right' :'left'}`]"></i>
                </button>
            </div>
        </div>

        <div class="static-aside-block">
            <ul>
                <li v-for="(value, key) in asideTabs " :key="key"  >
                    <button :class="['tab-btn',`${value.btnClass}`]" @click="openAsideTop(value.name, value.text)" >
                        <i :class="[`fas ${value.icon} `]"></i>
                        <br>
                        <span class="nav-text">
                            {{value.text}}
                        </span>
                    </button>
                </li>
                </ul>


            <ul class="bottom">
                <li v-for="(value, key) in bottomTabs" :key="key">
                    <button :class="['tab-btn', `${value.btnClass}`]" @click="openAsideTop(value.name, value.text)">
                        <i :class="[`fas ${value.icon}`]"></i>
                        <br>
                        <span class="nav-text">
                            {{value.text}}
                        </span>
                    </button>
                </li>
            </ul>
        </div>
    </div>
  </div>
</template>

<script>
import { v4 as uuidv4 } from 'uuid'

import searchTab from './components/search'
import textTab from './components/text'
import imagesTab from './components/images'
import fontTab from './components/font'
import templatesTab from './components/templates'

export default {
    name: 'app',
    mounted() {
        const inputsArr = localStorage.getItem('inputsArr')
        const output = localStorage.getItem('output')
        const bgImg = localStorage.getItem('bgImg')
        const bgColor = localStorage.getItem('bgColor')
        const bgPtrn = localStorage.getItem('bgPtrn')
        if(inputsArr){
            this.$root.inputsArr = JSON.parse(inputsArr)
        }
        if(output){
            this.output = output
        }
        if(this.$root.inputsArr.bgColor){
            this.$root.bgColor = this.$root.inputsArr.bgColor
        } else if (bgColor){
            this.$root.bgColor = bgColor
        }
        if(this.$root.inputsArr.bgImg){
            this.$root.bgImg = this.$root.inputsArr.bgImg
        } else if (bgImg){
            this.$root.bgImg = bgImg
        }
        if(this.$root.inputsArr.bgPtrn) {
            this.$root.bgPtrn = this.$root.inputsArr.bgPtrn
        } else if(bgPtrn) {
            this.$root.bgPtrn = bgPtrn
        }
    },
    data(){
        return {
            isActive: false,
            selectedComponent: '',
            componentInput: '',
            coorText: [],
            coorImg: [],
            xNum: 500,
            yNum: 300,
            img: [],
            type:'',
            output: null,
            isActiveEl: {
                img: false,
                text: false
            },
            activeElemType: '',
            activeElemArrIndex: 0,
            asideTabs: [
                {
                    name: 'search-tab', btnClass: 'search-btn', text: 'Search', icon: 'fa-search fa-2x'
                },
                {
                    name: 'text-tab', btnClass: 'text-btn', text: '텍스트', icon: 'fa-text-height fa-2x'
                },
                {
                    name: 'images-tab', btnClass: 'images-btn', text: '이미지', icon: 'fa-images fa-2x'
                },
                {
                    name: 'font-tab', btnClass: 'Font-btn', text: '배경', icon: 'fa-fill-drip fa-2x'
                }
            ],
            bottomTabs:[
                {
                    name: 'templates-tab', btnClass: 'Templates-btn', text: 'Templates', icon: 'fa-pen-square fa-2x'
                },
            ],
            countChange: 0

        }
    },

    components: {
        searchTab,
        textTab,
        imagesTab,
        fontTab,
        templatesTab
    },


    methods: {
        async print() {
            const el = this.$refs.printMe
            const options = {
                type: 'dataURL'
            }
            const outputlocal = await this.$html2canvas(el, options)

            this.output = outputlocal
            const link = document.createElement('a')
            link.href = outputlocal
            link.download= "image.jpg"
            document.body.appendChild(link)
            link.click()

            localStorage.setItem('output', this.output)
        },

        toggleAside(){
            this.isActive = !this.isActive;
        },

        openAsideTop(name, text){
            this.isActive = true;
            this.selectedComponent = name;
            this.componentInput = text;
        },

        pushElement(value, type){
            const item = {id: uuidv4(), x: 100, y: 100, w: 100, h: 100, angle: 0, src:value, type: type}
            this.$root.inputsArr.items.push(item)
            this.updateLocalStorage()
        },
        updateLocalStorage(){
            this.$_.debounce(function() {
                this.$root.inputsArr.bgImg = this.$root.bgImg
                this.$root.inputsArr.bgColor = this.$root.bgColor
                this.$root.inputsArr.bgPtrn = this.$root.bgPtrn
                localStorage.setItem('inputsArr', JSON.stringify(this.$root.inputsArr))

                const clone = this.$_.cloneDeep(this.$root.inputsArr)
                console.log('oldInputs before', clone)
                this.$root.changeHistory = [
                    clone,
                    ...this.$root.changeHistory
                ]

                console.log(this.$root.changeHistory)
            }, 300)
        },
        onCoordinatesChanged(item, index){
            this.$root.inputsArr.items[index] = item
            this.updateLocalStorage()
        },

        backChange () {
            this.$root.inputsArr = this.$_.cloneDeep(this.$root.changeHistory[++this.countChange])
            console.log(this.$root.changeHistory[this.countChange])

            this.$root.bgImg = this.$root.inputsArr.bgImg
            this.$root.bgColor = this.$root.inputsArr.bgColor
            this.$root.bgPtrn = this.$root.inputsArr.bgPtrn
            console.log('back change', this.$root.changeHistory[this.countChange])
            this.updateLocalStorage()
        },
        frontChange() {
            this.$root.inputsArr = this.$root.changeHistory[--this.countChange]
            console.log('front change', this.$root.changeHistory[this.countChange])
            this.updateLocalStorage()
        }
    },
    computed: {
        giveBackColor() {
            console.log(this.$backColor, 'asasas')
            return this.$backColor
        }
    },

}
</script>

<style>
*{
    user-select: none;
}
.hori{
    border-top: 2px dashed;
    width: 100%;
    position: absolute;
}
.printing-body {
    position: absolute;
    width: 800px;
    height: 500px;
    border: 8px solid #ccc;
    overflow: hidden;
}
.continue_button{
    cursor: pointer;
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
