/* eslint-disable */
import { ICategoryData } from '@/api/types'
import Vue from 'vue';

export default class RestApiCategory {

  static getCategory = (categoryId: string | null) => {
    console.log('get Category')
    return Vue.prototype.$axios.get(`/catalog/categories/${categoryId}`)
  };

  static getCategories = (search: string) => {
    console.log('get Categories')
    return Vue.prototype.$axios.get(`/catalog/categories`, {
      params: {
        search
      }
    })
  };
}
