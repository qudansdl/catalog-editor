<template>
  <q-layout view="lHh Lpr lFf">
    <q-drawer
      v-model="showMenu"
      show-if-above
      bordered
      content-class="bg-grey-1"
      side="right"
    >
      <q-layout view="lhr lpr lfr">

        <q-header elevated height-hint="98" class="bg-white text-primary">
          <q-list>
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="글자" icon="text_fields" @click="text.show = true"/>
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
            <q-item clickable v-ripple>
              <q-btn flat :disable="!enableDelete"  color="primary" label="Delete" icon="delete" />
            </q-item>
            <q-separator dark inset  color="orange" />
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Save" icon="save_alt" />
            </q-item>
            <q-separator dark inset  color="orange" />
            <q-item clickable v-ripple>
              <q-btn flat color="primary" label="Template" icon="file_copy" />
            </q-item>
            <q-item clickable v-ripple>
              <q-btn
                flat
                color="primary"
                icon="menu"
                label="Hide Menu"
                @click="showMenu  = !showMenu"
              />
            </q-item>
          </q-list>
        </q-footer>

      </q-layout>

    </q-drawer>

    <q-page-container>
      <div
        class="printing-body absolute-center"
        ref="printMe"
        :style="[{'background': configuration.backgroundPattern ? `url(${configuration.backgroundPattern}) repeat` : `${configuration.backgroundColor || '#ffffff'} url(${configuration.backgroundImg}) 0 0/cover no-repeat`}]">

        <template v-for="(item, idx) in configuration.items">
          <dr
            :key="item.id"
            :item="item"
            @coordinate="onCoordinatesChanged"
            :items="configuration.items"
            :itemIndex="idx">
          </dr>
        </template>
      </div>

      <q-page-sticky position="bottom-right" :offset="[18, 18]" v-if="showMenu == false">
        <q-btn fab icon="menu" color="accent" @click="showMenu = true"/>
      </q-page-sticky>
    </q-page-container>

    <edit-text v-model="text" v-on:apply="addElement"></edit-text>
  </q-layout>
</template>

<script lang="ts" src="./Index.ts" />

<style>
  *{
    user-select: none;
  }
  .printing-body {
    position: absolute;
    width: 800px;
    height: 500px;
    border: 8px solid #ccc;
    overflow: hidden;
  }
  .continue_button a{
    width: 100%;
    font-size: 20px;
    background: transparent;
    display: inline-block;
    line-height: 38px;
    text-transform: uppercase;
    font-weight: bold;
    color: #fff;
    padding: 0 20px;
    -webkit-transition: all .5s ease-out;
    transition: all .5s ease-out;
  }
</style>
