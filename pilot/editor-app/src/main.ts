import Vue from 'vue';
import { DirectiveOptions } from "vue";
import App from './App.vue';

import * as directives from './directives';

// Register global directives
Object.keys(directives).forEach(key => {
  Vue.directive(key, (directives as { [key: string ]: DirectiveOptions })[key]);
});

import _ from 'lodash'
Object.defineProperty(Vue.prototype, '$_', { value: _ });

import tinymce from 'tinymce';
import VueTinymce from '@packy-tang/vue-tinymce';

import 'tinymce/skins/content/default/content.min.css';
import 'tinymce/skins/ui/oxide/skin.min.css';
import 'tinymce/skins/ui/oxide/content.min.css';

import 'tinymce/themes/silver';

import 'tinymce/plugins/link';
import 'tinymce/plugins/image';
import 'tinymce/plugins/media';
import 'tinymce/plugins/table';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/quickbars';
import 'tinymce/plugins/fullscreen';

import '@/utils/tinymce/langs/ko_KR.js';

Object.defineProperty(Vue.prototype, '$tinymce', { value: tinymce });
Vue.use(VueTinymce);


import '@/assets/css/bootstrap.min.css';
import '@/assets/css/style.css';
import VueDraggableResizable from 'vue-draggable-resizable';
// @ts-ignore
import drr from '@minogin/vue-drag-resize-rotate';
import dr from '@/components/drr.vue';

import VueHtml2Canvas from 'vue-html2canvas';
Vue.use(VueHtml2Canvas);

// optionally import default styles
import 'vue-draggable-resizable/dist/VueDraggableResizable.css';

Vue.component('vue-draggable-resizable', VueDraggableResizable);
Vue.component('drr', drr);
Vue.component('dr', dr);

Vue.config.productionTip = false;
// Vue.config.strict = false

new Vue({
  data: () => ({
    bgColor: '' ,
    bgImg: '',
    bgPcImages: [],
    pcImages: [],
    urlImg: [],
    bgPtrn: '',
    changeHistory: [],
    inputsArr: {
      bgImg: '',
      bgColor: '',
      bgPtrn: '',
      items: []
    },
    setterState: false,
  }),
  render: h => h(App),
}).$mount('#app');
