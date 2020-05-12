import Vue from 'vue';
import Vuex from 'vuex';
import { getModule } from 'vuex-module-decorators';

import AppStatus from './modules/status';


Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    AppStatus,
  },
  strict: !process.env.DEV,
});
export default store;
export const AppStatusModule = getModule(AppStatus, store);
