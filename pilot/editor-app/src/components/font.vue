<template>
    <div>
        <div class="row">
            <button class="col-3 font-btn" v-for="item of components" :key="item" @click="activeFont(item)">{{item}}</button>
        </div>
        <br>
        <component :is="selectedComponent"></component>
    </div>
</template>

<script>
import pen from './font-bar/font-pen'
import pc from './font-bar/font-pc'
import lib from './font-bar/font-lib'
import ptrn from './font-bar/font-ptrn'
export default {
    data() {
        return {
            selectedComponent: 'pen',
            components: [
                'pen', 'pc', 'lib', 'ptrn'
            ],
        }
    },
    components: {
        pen,
        pc,
        lib,
        ptrn
    },
    methods: {
        activeFont(item) {
            this.selectedComponent = item
        },
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
