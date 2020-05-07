<template>
  <q-layout view="lHh Lpr lFf">
    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      content-class="bg-grey-1"
      side="right"
    >
      <q-layout view="lhr lpr lfr">

        <q-header elevated height-hint="98" class="bg-white text-primary">
          <q-list>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="글자" icon="text_fields"/>
            </q-item>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="그림" icon="image_search"/>
            </q-item>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="배경" icon="grid_on"/>
            </q-item>
          </q-list>
        </q-header>

        <q-footer elevated class="bg-white text-primary">
          <q-list>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Undo" icon="undo"/>
            </q-item>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Redo" icon="redo"/>
            </q-item>
            <q-separator dark vertical />
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Template" icon="file_copy" />
            </q-item>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Save" icon="save_alt" />
            </q-item>
            <q-separator dark vertical />
            <q-item clickable v-ripple>
              <q-btn
                flat
                color="primary"
                icon="menu"
                label="Hide Menu"
                @click="leftDrawerOpen = !leftDrawerOpen"
              />
            </q-item>
          </q-list>
        </q-footer>

      </q-layout>

    </q-drawer>

    <q-page-container>
      <router-view />
      <q-page-sticky position="bottom-right" :offset="[18, 18]" v-if="leftDrawerOpen == false">
        <q-btn fab icon="menu" color="accent" @click="leftDrawerOpen = true"/>
      </q-page-sticky>
    </q-page-container>
  </q-layout>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import EssentialLink from 'components/EssentialLink.vue';

@Component({
  components: {
    EssentialLink,
  },
})
export default class MainLayout extends Vue {
  leftDrawerOpen = false;
}
</script>
