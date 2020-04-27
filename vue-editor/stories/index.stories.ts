/* eslint-disable react/react-in-jsx-scope, react/no-this-in-sfc */

// @ts-ignore
import { storiesOf } from "@storybook/vue";
// @ts-ignore
import { linkTo } from "@storybook/addon-links";

import Welcome from "./Welcome";

// @ts-ignore
import MyButton from "@/components/MyButton.vue";

// @ts-ignore
import drr from '@/components/drr.vue';
// @ts-ignore
import TextBox from './components/TextBox';
// @ts-ignore
import CatalogEditor from '@/CatalogEditor.vue';


storiesOf('CatalogEditor', module)
    .add('with editor', () => ({
        components: { CatalogEditor },
        template: '<CatalogEditor></CatalogEditor>',
    }))
    .add('with image', () => ({
      components: { drr },
      template: '<drr\n' +
          '        :x="300"\n' +
          '        :y="300"\n' +
          '        :w="400"\n' +
          '        :h="267"\n' +
          '        :aspectRatio="true"\n' +
          '    >\n' +
          '      <img src="./stories/assets/img/8s.jpg" class="image"/>\n' +
          '    </drr>',
    }))
    .add('with placeholder', () => ({
      components: { drr },
      template: '    <drr\n' +
          '        :x="700"\n' +
          '        :y="400"\n' +
          '        :w="300"\n' +
          '        :h="200"\n' +
          '    >\n' +
          '      <img src="./stories/assets/img/placeholder.png" class="image"/>\n' +
          '    </drr>',
    }))
    .add('with textbox', () => ({
      components: { drr, TextBox },
      template: '<drr\n' +
          '        :x="400"\n' +
          '        :y="600"\n' +
          '        :w="200"\n' +
          '        :h="100"\n' +
          '        :hasActiveContent="true"\n' +
          '    >\n' +
          '      <TextBox/>\n' +
          '    </drr>',
    }));


storiesOf("Welcome", module).add("to Storybook", () => ({
  components: { Welcome },
  template: '<welcome :showApp="action" />',
  methods: { action: linkTo("Button") }
}));

storiesOf("Button", module)
  .add("My first button story", () => ({
    components: { MyButton },
    template: `
 <my-button></my-button>
  `
  }))
  .add("Now with some props", () => ({
    components: { MyButton },
    template: `
 <my-button :msg="myMessage" :color="myColor"></my-button>
  `,
    data() {
      return {
        myMessage: "This is finally working",
        myColor: "blue"
      };
    }
  }));

/* eslint-enable react/react-in-jsx-scope */
