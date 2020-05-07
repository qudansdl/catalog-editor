<template>
    <div>
        <div v-for="n in images" :key="n">
            <button @click="setBgImg(n)">
                <img :src="n" alt="">
            </button>
        </div>
    </div>
</template>

<script>
import { cloneDeep } from 'lodash';
import ptn1 from '@/assets/img/pattern/pattern1.jpg';
import ptn2 from '@/assets/img/pattern/pattern2.jpg';
import ptn3 from '@/assets/img/pattern/pattern3.jpg';
import ptn4 from '@/assets/img/pattern/pattern4.png';
import ptn5 from '@/assets/img/pattern/1.jpg';
import ptn6 from '@/assets/img/pattern/2.jpg';
import ptn7 from '@/assets/img/pattern/images.jpg';


export default {
  data() {
    return {
      images: [ptn1, ptn2, ptn3, ptn4, ptn5, ptn6, ptn7],
    };
  },
  methods: {
    setBgImg(n) {
      this.$root.bgPtrn = n;
      this.$root.inputsArr.bgPtrn = n;

      localStorage.setItem('inputsArr', JSON.stringify(this.$root.inputsArr));
      localStorage.setItem('bgPtrn', JSON.stringify(this.$root.bgPtrn));

      const clone = cloneDeep(this.$root.inputsArr);
      console.log('oldInputs before', clone);

      this.$root.sequenceOfChange.unshift(clone);
    },
  },
};
</script>

<style>

</style>
