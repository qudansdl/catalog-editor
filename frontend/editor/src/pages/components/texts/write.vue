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
              'Frutiger',
              'GodoM',
              'GodoB',
              'NanumBarunGothic',
              'yun310',
              'yun320',
              'yun330',
              'yun340',
              'yun350',
              'yun360'
            ]
          },
          'removeFormat'
        ],
        ['quote'],
        ['undo', 'redo'],
        ['viewsource']
      ]"
      :fonts="{
        Frutiger: 'Frutiger',
        GodoM: '고도체M',
        GodoB: '고도체B',
        NanumBarunGothic: '나눔바른고딕',
        yun310: '윤고딕310',
        yun320: '윤고딕320',
        yun330: '윤고딕330',
        yun340: '윤고딕340',
        yun350: '윤고딕350',
        yun360: '윤고딕360'
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
