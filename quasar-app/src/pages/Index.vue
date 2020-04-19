<template>
  <q-page class="flex flex-center">
    <iframe ref="content" :src="contentFrame.src" style="position: absolute; overflow:hidden;height:100%;width:100%" height="100%" width="100%" frameborder="0" wmode="transparent"  @load="contentLoaded"></iframe>
  </q-page>
</template>
<script>
import { EventBus } from '../utils/eventBus'

export default {
  name: 'PageIndex',
  data () {
    return {
      contentFrame: {
        src:  process.env.WEB_APP_URL+'?rs=' + new Date().getTime(),
        loaded: false
      }
    }
  },
  mounted() {
    this.$receiveMessage((evt) => {
      console.log("message received" + JSON.stringify(evt.data))
      if(evt.data.type === 'EXECUTE_EDITOR')
      {
        console.log("execute editor received")
      }
    }, false)
  },
  methods: {
    contentLoaded () {
      this.contentFrame.loaded = true

      if(this.fcmToken)
      {
        console.log("calling existing token")
        this.$postMessage({type:'FCM_TOKEN', token: this.fcmToken}, '*', this.$refs.content.contentWindow)
      }

      EventBus.$emit('contentFrameReady')
    }
  },
  watch: {
    fcmToken(newVal, oldVal) {
      console.log("received updated token")
      this.$postMessage({type:'FCM_TOKEN', token: newVal}, '*', this.$refs.content.contentWindow)
    }
  },
  computed: {
    fcmToken: function() {
      return this.$store.getters.token;
    }
  }
}
</script>

