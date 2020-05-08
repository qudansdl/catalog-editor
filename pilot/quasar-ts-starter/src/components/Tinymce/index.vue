<template>
  <div
    :class="{fullscreen: fullscreen}"
    class="tinymce-container"
    :style="{width: containerWidth, height: '90%'}"
  >
    <tinymce-editor
      :id="id"
      v-model="tinymceContent"
      :init="initOptions"
    />
  </div>
</template>

<script lang="ts">
// Docs: https://armour.github.io/vue-typescript-admin-docs/features/components/rich-editor.html#tinymce
import 'tinymce/tinymce';
import 'tinymce/themes/silver'; // Import themes
import 'tinymce/themes/mobile';
import 'tinymce/plugins/advlist'; // Any plugins you want to use has to be imported
import 'tinymce/plugins/anchor';
import 'tinymce/plugins/autolink';
import 'tinymce/plugins/autosave';
import 'tinymce/plugins/code';
import 'tinymce/plugins/codesample';
import 'tinymce/plugins/directionality';
import 'tinymce/plugins/emoticons';
import 'tinymce/plugins/fullscreen';
import 'tinymce/plugins/hr';
import 'tinymce/plugins/image';
import 'tinymce/plugins/imagetools';
import 'tinymce/plugins/insertdatetime';
import 'tinymce/plugins/link';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/media';
import 'tinymce/plugins/nonbreaking';
import 'tinymce/plugins/noneditable';
import 'tinymce/plugins/pagebreak';
import 'tinymce/plugins/paste';
import 'tinymce/plugins/preview';
import 'tinymce/plugins/print';
import 'tinymce/plugins/save';
import 'tinymce/plugins/searchreplace';
import 'tinymce/plugins/spellchecker';
import 'tinymce/plugins/tabfocus';
import 'tinymce/plugins/table';
import 'tinymce/plugins/template';
import 'tinymce/plugins/textpattern';
import 'tinymce/plugins/visualblocks';
import 'tinymce/plugins/visualchars';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/autoresize';
import '@/statics/tinymce/plugins/autoheight/index';

import TinymceEditor from '@tinymce/tinymce-vue'; // TinyMCE vue wrapper
import {
  Component, Prop, Vue, Watch,
} from 'vue-property-decorator';
import { plugins, toolbar } from './config';

const defaultId = () => `vue-tinymce-${+new Date()}${(Math.random() * 1000).toFixed(0)}`;

@Component({
  name: 'Tinymce',
  components: {
    TinymceEditor,
  },
})
export default class extends Vue {
  @Prop({ required: true }) private value!: string;

  @Prop({ default: defaultId }) private id!: string;

  @Prop({ default: () => [] }) private toolbar!: string[];

  @Prop({ default: 'file edit insert view format table' }) private menubar!: string;

  @Prop({ default: 'inherit' }) private minHeight!: string | number;

  @Prop({ default: 'auto' }) private width!: string | number;

  private hasChange = false;

  private hasInit = false;

  private fullscreen = false;


  get tinymceContent() {
    return this.value;
  }

  set tinymceContent(value) {
    this.$emit('input', value);
  }

  get containerWidth() {
    const { width } = this;
    // Test matches `100`, `'100'`
    if (/^[\d]+(\.[\d]+)?$/.test(width.toString())) {
      return `${width}px`;
    }
    return width;
  }

  get initOptions() {
    return {
      selector: `#${this.id}`,
      min_height: this.minHeight,
      autoresize_on_init: true,
      body_class: 'panel-body ',
      object_resizing: false,
      branding: false,
      toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
      menubar: this.menubar,
      plugins,
      language: 'ko_KR',
      language_url: '/statics/tinymce/langs/ko_KR.js',
      skin_url: '/statics/tinymce/skins/ui/oxide/',
      content_css: '/statics/tinymce/skins/content/default',
      emoticons_database_url: '/statics/tinymce/plugins/emoticons/emojis.js',
      end_container_on_empty_block: true,
      powerpaste_word_import: 'clean',
      advlist_bullet_styles: 'square',
      advlist_number_styles: 'default',
      default_link_target: '_blank',
      link_title: false,
      nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
      init_instance_callback: (editor: any) => {
        if (this.value) {
          editor.setContent(this.value);
        }
        this.hasInit = true;
        editor.on('NodeChange Change KeyUp SetContent', () => {
          this.hasChange = true;
          this.$emit('input', editor.getContent());
        });
      },
      setup: (editor: any) => {
      },
    };
  }

  @Watch('language')
  private onLanguageChange() {
    const tinymceManager = (window as any).tinymce;
    const tinymceInstance = tinymceManager.get(this.id);
    if (this.fullscreen) {
      tinymceInstance.execCommand('mceFullScreen');
    }
    if (tinymceInstance) {
      tinymceInstance.destroy();
    }
    this.$nextTick(() => tinymceManager.init(this.initOptions));
  }
}
</script>

<style lang="scss" scoped>
.tinymce-container {
  position: relative;
  line-height: normal;

  .mce-fullscreen {
    z-index: 10000;
  }
}

.editor-custom-btn-container {
  position: absolute;
  right: 6px;
  top: 6px;

  &.fullscreen {
    z-index: 10000;
    position: fixed;
  }
}

.editor-upload-btn {
  display: inline-block;
}

textarea {
  visibility: hidden;
  z-index: -1;
}
</style>

<style lang="scss">
  .tox-silver-sink {
    z-index: 70000 !important;
  }
</style>
