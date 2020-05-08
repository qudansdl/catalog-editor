import Vue from 'vue';
import Component from 'vue-class-component';

import { v4 as uuidv4 } from 'uuid';
import { debounce, cloneDeep } from 'lodash';
import html2canvas from 'html2canvas';

import VueDraggableResizable from 'vue-draggable-resizable';

import drr from '@minogin/vue-drag-resize-rotate';
import dr from '@/components/drr.vue';

// optionally import default styles
import 'vue-draggable-resizable/dist/VueDraggableResizable.css';

import { Item, Configuration } from '@/types/types';

import SearchTab from '@/components/search.vue';
import ImagesTab from '@/components/images.vue';
import FontTab from '@/components/font.vue';
import TemplatesTab from '@/components/templates.vue';

import EditText from './components/Text.vue';

@Component({
  components: {
    SearchTab,
    EditText,
    ImagesTab,
    FontTab,
    TemplatesTab,
    VueDraggableResizable,
    drr,
    dr,
  },
})
export default class Index extends Vue {
  text = {
    show: false,
    content: '<p>여기에 내용을 입력하세요</p>',
  };

  showMenu = false;

  countChange = 0;

  configurationHistory = [] as Configuration[];

  configuration: Configuration = {
    backgroundColor: '',
    backgroundImg: '',
    backgroundPattern: '',
    items: [],
  };

  get enableDelete() {
    return Math.random()
  }

  public mounted(): void {
    const configuration = localStorage.getItem('configuration');
    if (configuration) {
      this.configuration = JSON.parse(configuration);
    }
  }

  async print() {
    const el = this.$refs.printMe;
    await html2canvas(el as HTMLElement).then(
      (canvas) => {
        console.log('canvas', canvas);
        console.log('dataUrl', canvas.toDataURL());

        const link = document.createElement('a');
        link.href = canvas.toDataURL();
        link.download = 'image.jpg';
        document.body.appendChild(link);
        link.click();
      },
    );
  }

  addElement(newItem: Item) {
    const item: Item = {
      id: uuidv4().toUpperCase(),
      x: 100,
      y: 100,
      w: 100,
      h: 100,
      angle: 0,
      src: newItem.src,
      type: newItem.type,
      selected: true,
    };

    this.configuration.items.push(item);
    this.updateLocalStorage();
  }

  updateLocalStorage() {
    const { configuration } = this;
    debounce(() => {
      localStorage.setItem('configuration', JSON.stringify(configuration));

      const clone: Configuration = cloneDeep(configuration);
      console.log('old config before', clone);
      this.configurationHistory.unshift(clone);
      console.log(this.configurationHistory);
    }, 300);
  }

  onCoordinatesChanged(item: Item, index: number) {
    this.configuration.items[index] = item;
    this.updateLocalStorage();
  }

  undo() {
    this.countChange += 1;
    this.configuration = cloneDeep(this.configurationHistory[this.countChange]);
    console.log(this.configurationHistory[this.countChange]);

    console.log('back change', this.configurationHistory[this.countChange]);
    this.updateLocalStorage();
  }

  redo() {
    this.countChange -= 1;
    this.configuration = this.configurationHistory[this.countChange];
    console.log('front change', this.configurationHistory[this.countChange]);
    this.updateLocalStorage();
  }
}
