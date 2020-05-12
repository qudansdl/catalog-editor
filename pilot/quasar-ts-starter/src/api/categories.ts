import { GET_CATEGORIES } from '@/api/graphql/category';
import Vue from 'vue';

export default class ApiCategory {
  static getCategories = () => Vue.prototype.$apollo.query({
    query: GET_CATEGORIES,
  });
}
