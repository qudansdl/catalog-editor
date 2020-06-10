<template>
  <q-card>
    <q-card-section class="row justify-center">
      <q-color
        v-model="color"
        @change="setSelected"/>
    </q-card-section>
  </q-card>
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
