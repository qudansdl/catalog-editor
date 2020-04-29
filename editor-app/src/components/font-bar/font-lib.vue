<template>
    <div>
        <div v-for="n in images" :key="n">
            <button @click="setBgImg(n)">
                <img :src="n" alt="">
            </button>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            images: [
                require('@/assets/img/download.jpg'),
                require('@/assets/img/images.jpg'),
                require('@/assets/img/1.jpg')
            ]
        }
    },
    methods: {
        setBgImg(n) {
            this.$root.bgImg = n
            this.$root.bgPtrn = ''
            this.$root.inputsArr.bgImg = n

            localStorage.setItem('inputsArr', JSON.stringify(this.$root.inputsArr))
            localStorage.removeItem('bgPtrn')
            localStorage.setItem('bgImg', JSON.stringify(this.$root.bgImg))

            const clone = _.cloneDeep(this.$root.inputsArr)
            console.log('oldInputs before', clone)
            // Har o'zgarishni arrayga qowiw
            this.$root.sequenceOfChange.unshift(clone)
        }
    }
}
</script>

<style scoped>
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
