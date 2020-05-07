import Vue from 'vue';
import Component from 'vue-class-component';

import { v4 as uuidv4 } from 'uuid';
import { debounce, cloneDeep } from 'lodash';
import html2canvas from 'html2canvas';

import '@/assets/css/bootstrap.min.css';
import '@/assets/css/style.css';
import VueDraggableResizable from 'vue-draggable-resizable';

import drr from '@minogin/vue-drag-resize-rotate';
import dr from '@/components/drr.vue';

// optionally import default styles
import 'vue-draggable-resizable/dist/VueDraggableResizable.css';

import { Item, Configuration } from '@/types/types';

import SearchTab from '@/components/search.vue';
import TextTab from '@/components/text.vue';
import ImagesTab from '@/components/images.vue';
import FontTab from '@/components/font.vue';
import TemplatesTab from '@/components/templates.vue';

@Component({
  components: {
    SearchTab,
    TextTab,
    ImagesTab,
    FontTab,
    TemplatesTab,
    VueDraggableResizable,
    drr,
    dr,
  },
})
export default class Index extends Vue {
  isActive = false;

  selectedComponent = '';

  asideTabs: Array<object> = [
    {
      name: 'search-tab', btnClass: 'search-btn', text: 'Search', icon: 'fa-search fa-2x',
    },
    {
      name: 'text-tab', btnClass: 'text-btn', text: '텍스트', icon: 'fa-text-height fa-2x',
    },
    {
      name: 'images-tab', btnClass: 'images-btn', text: '이미지', icon: 'fa-images fa-2x',
    },
    {
      name: 'font-tab', btnClass: 'font-btn', text: '배경', icon: 'fa-fill-drip fa-2x',
    },
  ];

  bottomTabs: Array<object> = [
    {
      name: 'templates-tab', btnClass: 'templates-btn', text: '템플릿', icon: 'fa-pen-square fa-2x',
    },
  ];

  countChange = 0;

  configurationHistory = [] as Configuration[];

  configuration: Configuration = {
    backgroundColor: '',
    backgroundImg: '',
    backgroundPattern: '',
    items: [],
  };

  public mounted(): void {
    const configuration = localStorage.getItem('configuration');
    if (configuration) {
      this.configuration = JSON.parse(configuration);
    }
  }

  async print() {
    const el = this.$refs.printMe;
    const options = {
      type: 'dataURL',
    };
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

  toggleAside() {
    this.isActive = !this.isActive;
  }

  openAsideTop(name: string, text: string) {
    this.isActive = true;
    this.selectedComponent = name;
  }

  pushElement(value: string, type: string) {
    const item: Item = {
      id: uuidv4().toUpperCase(),
      x: 100,
      y: 100,
      w: 100,
      h: 100,
      angle: 0,
      src: value,
      type,
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
