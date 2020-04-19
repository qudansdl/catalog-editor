import { Vue, Component, Prop } from 'vue-property-decorator';
import { DirectiveOptions } from "vue";

import * as directives from './directives'

// @ts-ignore
import drr from './components/drr.vue';
// @ts-ignore
import TextBox from './components/TextBox.vue';

// @ts-ignore
import ImageDialog from "./components/ImageDialog.vue";

// Register global directives
Object.keys(directives).forEach(key => {
  Vue.directive(key, (directives as { [key: string ]: DirectiveOptions })[key])
})

@Component({
  name: 'CatalogEditor',
  components: {
    drr,
    TextBox,
    ImageDialog
  }
})
export default class CatalogEditor extends Vue {
  showImageDialog = false
  mounted (){
    console.log('hello from app');
  }

  private closeImageDialog(){
    this.showImageDialog = false
  }

}

