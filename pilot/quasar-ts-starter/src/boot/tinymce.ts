import Vue from 'vue';

import tinymce from 'tinymce';
import VueTinymce from '@packy-tang/vue-tinymce';

import 'tinymce/skins/content/default/content.css';
import 'tinymce/skins/ui/oxide/skin.css';
import 'tinymce/skins/ui/oxide/content.css';

import 'tinymce/themes/silver';

import 'tinymce/plugins/link';
import 'tinymce/plugins/image';
import 'tinymce/plugins/media';
import 'tinymce/plugins/table';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/quickbars';
import 'tinymce/plugins/fullscreen';

import '@/utils/tinymce/langs/ko_KR';

console.log('AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA');
Object.defineProperty(Vue.prototype, '$tinymce', { value: tinymce });
Vue.use(VueTinymce);
