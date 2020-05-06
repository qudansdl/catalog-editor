<template>
  <q-layout view="lHh Lpr lFf">
    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      content-class="bg-grey-1"
      side="right"
    >
      <q-list bordered>
        <q-fixed-position corner="top-right" :offset="[18, 18]">

          <q-item clickable v-ripple>
            <q-btn flat color="primary" label="글자" icon="text_fields"/>
          </q-item>
          <q-item clickable v-ripple>
            <q-btn flat color="primary" label="그림" icon="image_search"/>
          </q-item>
          <q-item clickable v-ripple>
            <q-btn flat color="primary" label="배경" icon="grid_on"/>
          </q-item>

        </q-fixed-position>
        <q-fixed-position corner="bottom-right" :offset="[18, 18]">
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
        </q-fixed-position>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
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
