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
                require('@/assets/img/pattern/pattern1.jpg'),
                require('@/assets/img/pattern/pattern2.jpg'),
                require('@/assets/img/pattern/pattern3.jpg'),
                require('@/assets/img/pattern/pattern4.png'),
                require('@/assets/img/pattern/1.jpg'),
                require('@/assets/img/pattern/2.jpg'),
                require('@/assets/img/pattern/images.jpg'),
            ]
        }
    },
    methods: {
        setBgImg(n) {
            this.$root.bgPtrn = n
            this.$root.inputsArr.bgPtrn = n

            localStorage.setItem('inputsArr', JSON.stringify(this.$root.inputsArr))
            localStorage.setItem('bgPtrn', JSON.stringify(this.$root.bgPtrn))

            const clone = _.cloneDeep(this.$root.inputsArr)
            console.log('oldInputs before', clone)
            // Har o'zgarishni arrayga qowiw
            this.$root.sequenceOfChange.unshift(clone)
        }
    }
}
</script>

<style>

</style>
