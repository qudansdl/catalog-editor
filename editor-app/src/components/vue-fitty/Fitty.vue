<template>
  <div class="mainwrap" :id="contentID" >
    <slot name="content"  />
  </div>
    
</template>
<script>
import fitty from "fitty";

export default {
  name: "fitty",
  props: ["options"],
  data(){
    return {
      fitty: null,
    }
  },
  mounted() {    
      this.$nextTick(() => {
      this.init();
      
    });
  },
  computed: {
    contentID() {
      return "fitty-" + this._uid;
    }
  },
  methods: {
    init() {
      this.fitty = fitty("#" + this.contentID, this.options)[0]
    },
    fit() {
      this.fitty.fit()
    },
  }
};
</script>
<style scoped>
.mainwrap {
  position: relative;
}

.content {
  position: relative;
  overflow: hidden;
}

.svgoverlay {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  z-index: 2;
}
</style>


