<template>
    <div>
        <button @click="template">
            <img :src="lalala" alt="">
        </button>
    </div>
</template>

<script>
export default {
    props: ['lalala'],
    methods: {
        template() {
            this.$emit('newTemp', this.lalala)
        }
    },
}
</script>

<style>
    img{
        padding: 2px;
        width: 100%;
        z-index: 10000;
        
        height: auto
    }
    button {
        background: none;
        border: none;
        cursor: pointer;
        margin: auto;
        display: block;
    }
</style>
