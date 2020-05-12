<template>
    <div class="row p-4">
      <q-color v-model="color" />
    </div>
</template>

<script>
import { cloneDeep } from 'lodash';

export default {
  data() {
    return {
      color: null,
    };
  },
  methods: {
    callColor(item) {
      this.$root.bgColor = item;
      this.$root.bgImg = '';
      this.$root.bgPtrn = '';
      this.$root.inputsArr.bgColor = item;

      localStorage.setItem('inputsArr', JSON.stringify(this.$root.inputsArr));
      localStorage.removeItem('bgPtrn');
      localStorage.setItem('bgColor', item);
      localStorage.removeItem('bgImg');

      const clone = cloneDeep(this.$root.inputsArr);
      // Har o'zgarishni arrayga qowiw
      this.$root.sequenceOfChange.unshift(clone);
    },
    callPickerColor(item) {
      this.$root.bgColor = item.hex8;
      this.$root.bgImg = '';
      this.$root.bgPtrn = '';
      this.$root.inputsArr.bgColor = item.hex8;

      localStorage.setItem('inputsArr', JSON.stringify(this.$root.inputsArr));
      localStorage.removeItem('bgPtrn');
      localStorage.setItem('bgColor', item.hex8);
      localStorage.removeItem('bgImg');

      const clone = cloneDeep(this.$root.inputsArr);

      this.$root.sequenceOfChange.unshift(clone);
    },
  },
};
</script>

<style>
.color-btn {
    margin: 5px;
    height: 30px;
}
.picker {
    width: 100%;
}
</style>
