<template>
    <div>
        <a class="btn add-text" @click="addForm">추가</a>
        <vue-tinymce
                v-model="content"
                :setup="setup"
                :setting="setting" />
    </div>
</template>
<script>
export default {
    data: function() {
        return {
            content: "<p>내용</p>",
            setting: {
                menubar: false,
                inline: true,
                height: 500
            }
        }
    },
    methods: {
        addForm(){
            this.$emit('addText',this.content, 'text')
        },
        setup(editor) {
            console.log(editor)
        }
    },
}
</script>

<style>
.add-text {
    width: 100%;
    font-size: 20px;
    background: #dd0330;
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
