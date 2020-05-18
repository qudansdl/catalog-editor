<template>
    <div>
        <div class="row">
            <button class="col-4 font-btn" v-for="item of components" :key="item.name" @click="activeFont(item)">{{item.label}}</button>
        </div>
        <br>
        <component :is="selectedComponent" @addText="addText"></component>
    </div>
</template>

<script>
import pc from './img-bar/img-pc'
import lib from './img-bar/img-lib'
import url from './img-bar/img-url'
export default {
    data() {
        return {
            selectedComponent: 'lib',
            components: [
                {name: 'lib', label: '선택'},
                {name: 'pc', label: '업로드'},
                {name: 'url', label: 'URL'}
            ],
        }
    },
    components: {
        url,
        pc,
        lib,
    },
    methods: {
        activeFont(item) {
            this.selectedComponent = item.name
        },
        addText(img, type)
         {
            this.$emit('addText', img, type)
         }
    },
}
</script>

<style>
.font-btn {
    color: white;
    background-color: blue;
    border: 1px solid;
    height: 50px;
    padding: 3px 0;
    opacity: 0.2;
}
.font-btn:focus {
    opacity: 1;
}
</style>
