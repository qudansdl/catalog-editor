<template>
  <q-card>
    <q-card-section class="justify-center">
      <q-editor
        v-model="html"
        :dense="$q.screen.lt.md"
        :toolbar="[
        [
          {
            icon: $q.iconSet.editor.align,
            fixedLabel: true,
            options: ['left', 'center', 'right', 'justify']
          }
        ],
        ['bold', 'italic', 'strike', 'underline'],
        ['fullscreen'],
        [
          {
            icon: $q.iconSet.editor.formatting,
            list: 'no-icons',
            options: [
              'p',
              'h1',
              'h2',
              'h3',
              'h4',
              'h5',
              'h6',
              'code'
            ]
          },
          {
            icon: $q.iconSet.editor.fontSize,
            fixedLabel: true,
            fixedIcon: true,
            list: 'no-icons',
            options: [
              'size-1',
              'size-2',
              'size-3',
              'size-4',
              'size-5',
              'size-6',
              'size-7'
            ]
          },
          {
            icon: $q.iconSet.editor.font,
            fixedIcon: true,
            options: [
              'default_font',
              'dotum',
              'gullim',
              'batang',
              'NanumGothic',
              'MalgunGothic',
              'arial',
              'arial_black',
              'comic_sans',
              'courier_new',
              'impact',
              'lucida_grande',
              'times_new_roman',
              'verdana'
            ]
          },
          'removeFormat'
        ],
        ['quote'],
        ['undo', 'redo'],
        ['viewsource']
      ]"
        :fonts="{
        arial: 'Arial',
        arial_black: 'Arial Black',
        comic_sans: 'Comic Sans MS',
        courier_new: 'Courier New',
        impact: 'Impact',
        lucida_grande: 'Lucida Grande',
        times_new_roman: 'Times New Roman',
        verdana: 'Verdana',
        dotum: '돋움',
        gullim: '굴림',
        batang: '바탕',
        NanumGothic: '나눔고딕',
        MalgunGothic: '맑은고딕'
      }"
      />
    </q-card-section>
  </q-card>
</template>

<script>
  import { Component, Prop, Vue, Watch } from 'vue-property-decorator';

@Component
export default class WriteText extends Vue {
  @Prop({ required: true }) content;

  html = ""

  mounted(){
    this.html = this.content
  }
  @Watch('html')
  onContentChanged() {
    this.$emit('textSelected', this.html);
  }
};
</script>
