<template>
    <div class="p-2">
        <input ref="refInput" type="file" multiple hidden="hidden" accept=".png, .jpg, .svg" @change="onInputChange">
        <div class="continue_button" >
            <a type="#" @click="fileUpload">Upload file</a>
        </div>

        <div class="row p-0 m-0">
            <div class=" col-6 p-0 m-0" v-for="(item, k) of $root.pcImages" :key="k" >
                <div class=" img-btn" 
                @click="$emit('addText', item, 'img')"
                :style="{
                    height: '100px',
                    width: '100%',
                    backgroundImage : `url(${item})`,
                    backgroundSize: 'cover',
                    backgroundRepeat: 'no-repeat',
                    }">
                    <!-- <img id="Image" alt="" width="100%"> -->
                </div>
                <button class="delete-btn" @click="deleteBtn(k)">
                    &times;
                    </button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            img2B64 : '',
        }
    },
    methods: {
        fileUpload() {
            this.$refs.refInput.click()
        },
        onInputChange(e) {
            if(e.target.files){
                for(let file of e.target.files) {
                    const reader = new FileReader()
                    reader.onload = e => this.$root.pcImages.push(e.target.result)
                    reader.readAsDataURL(file)
                }
            }
        },
        fileToImage(item) {
            if(item) {
                return URL.createObjectURL(item) 
            }
        },
        imgToBase(item) {
            const reader = new FileReader()
            reader.onload = e => this.img2B64 = e.target.result
            reader.readAsDataURL(item)
            return this.img2B64
        },
        deleteBtn(item) {
            this.$root.pcImages.splice(item, 1)
        }
    },
}
</script>

<style>
.img-btn {
    cursor: pointer;
}
.delete-btn{
    position: absolute;
    width: 100%;
    right: 0;
    top: 0;
    color: transparent;
    font-size: 20px;
}
.delete-btn:hover {
    background: rgba(0, 0, 0, 0.329);
    color: white;
}
</style>