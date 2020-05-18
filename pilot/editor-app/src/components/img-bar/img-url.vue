<template>
    <div>
        <div class="img-url">
            <input type="text" placeholder="enter url" v-model="urlText">
            <button class="url-dwl" @click="dwnlImg">&dagger; </button>
        </div>
        <div class="row p-0 m-0">
            <div class=" col-6 p-0 m-0" v-for="(item, k) of $root.urlImg" :key="k" >
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
            urlText: ''
        }
    },
    methods: {
        dwnlImg() {
            if(!this.urlText) {
                alert("Please enter proper url")
            } else if (this.checkURL()) {
                alert("Please enter proper url")
            } else {
                this.$root.urlImg.push(this.urlText)
            }
        },
        deleteBtn(item) {
            this.$root.urlImg.splice(item, 1)
        },
        checkURL() {
            return(this.urlText.match(/\.(jpeg|jpg|gif|png)$/) != null);
        }
    },
    computed: {
        
    },
}
</script>

<style>
    .url-dwl {
        width: 50px;
        height: 50px;
        background: darkblue;
        font-size: 40px;
        color: white;
        transition-duration: 1s;
    }
    .url-dwl:hover {
        background-color: red;
        color: black;
    }
    .img-url {
        display: flex;
    }
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