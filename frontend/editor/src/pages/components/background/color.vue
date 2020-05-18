<template>
    <div class="row p-4">
      <q-color
        v-model="color"
        @change="setSelected"/>
    </div>
</template>

<script>

export default {
  data() {
    return {
      color: null,
    };
  },
  methods: {
    setSelected(img) {
      this.$emit('colorSelected', img);
    },
  },
};
</script>
