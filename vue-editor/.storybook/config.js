import { configure, addDecorator } from '@storybook/vue';
import "@/plugins/elementui";

addDecorator(() => {
  return {
    template: `
      <div>
          <story/>
      </div>
    `
  }

})


// automatically import all files ending in *.stories.ts
const req = require.context('../stories', true, /\.stories\.ts?$/);
function loadStories() {
  req.keys().forEach((filename) => req(filename));
}

configure(loadStories, module);
