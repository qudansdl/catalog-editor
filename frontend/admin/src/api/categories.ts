import {
  CREATE_CATEGORY,
  GET_CATEGORIES,
  GET_CATEGORY_BY_ID
} from '@/api/graphql/category';
import Vue from 'vue';

export default class ApiCategory {
  static createCategory = (name: string) => {
    console.log('Create Category');
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_CATEGORY,
      variables: {
        name,
      }
    });
  };

  static getCategory = (id?: string) => {
    console.log('get Category');
    return Vue.prototype.$apollo.query({
      query: GET_CATEGORY_BY_ID,
      variables: {
        id,
      }
    });
  };
}
