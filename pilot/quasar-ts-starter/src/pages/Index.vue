<template>
  <q-page class="flex flex-center">
      <div class="scnt">
        <div class="constructor_area">
          <div class="constructor_page_wrapper">
            <div class="constuctor_page__square" >
              <!-- square lines -->
              <div class="square__back__lines__cnt">
                <div class="back__line"></div>
                <div class="back__line"></div>
                <div class="back__line"></div>
                <div class="back__line"></div>
              </div>
              <div id="paper" class="constructor__inner__border__cnt">
                <div
                  class="printing-body"
                  ref="printMe"
                  :style="[{'background': configuration.backgroundPattern ? `url(${configuration.backgroundPattern}) repeat` : `${configuration.backgroundColor || '#ffffff'} url(${configuration.backgroundImg}) 0 0/cover no-repeat`}]">

                  <template v-for="(item, idx) in configuration.items">
                    <dr
                      :key="item.id"
                      :item="item"
                      @coordinate="onCoordinatesChanged"
                      :items="configuration.items"
                      :itemIndex="idx" >
                    </dr>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="side-ctn">
        <div :class="['aside-block', {active: isActive}]"  >
          <div class="aside-active-tab-cnt">
            <component :is="selectedComponent" @addText="pushElement"></component>
          </div>

          <div class="left_border">
            <button class="toggle-btn" @click="toggleAside">
              <i :class="[`fas fa-chevron-${!this.isActive ? 'right' :'left'}`]"></i>
            </button>
          </div>
        </div>

        <div class="static-aside-block">
          <ul>
            <li v-for="(value, key) in asideTabs " :key="key"  >
              <button :class="['tab-btn',`${value.btnClass}`]" @click="openAsideTop(value.name, value.text)">
                <i :class="[`fas ${value.icon} `]"></i>
                <br>
                <span class="nav-text">
                            {{value.text}}
                        </span>
              </button>
            </li>
          </ul>


          <ul class="bottom">
            <li v-for="(value, key) in bottomTabs" :key="key">
              <button :class="['tab-btn', `${value.btnClass}`]" @click="openAsideTop(value.name, value.text)">
                <i :class="[`fas ${value.icon}`]"></i>
                <br>
                <span class="nav-text">{{value.text}}</span>
              </button>
            </li>
          </ul>
        </div>
      </div>
  </q-page>
</template>

<script lang="ts" src="./Index.ts" />

<style>
  *{
    user-select: none;
  }
  .hori{
    border-top: 2px dashed;
    width: 100%;
    position: absolute;
  }
  .printing-body {
    position: absolute;
    width: 800px;
    height: 500px;
    border: 8px solid #ccc;
    overflow: hidden;
  }
  .continue_button{
    cursor: pointer;
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
