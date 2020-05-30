/* eslint-disable */
import {
  CREATE_CATEGORY, DELETE_CATEGORY,
  GET_CATEGORIES,
  GET_CATEGORY_BY_ID, UPDATE_CATEGORY
} from '@/api/graphql/category'
import { ICategoryData } from '@/api/types'
import Vue from 'vue';

export const defaultCategoryData: ICategoryData = {
  id: null,
  name: '',
  parent: null
}

export default class ApiCategory {
  static createCategory = (name: string, parentId: string | null) => {
    console.log('Create Category')
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_CATEGORY,
      variables: {
        name,
        parentId
      }
    })
  };

  static updateCategory = (cat: ICategoryData) => {
    console.log('Update Category')
    return Vue.prototype.$apollo.mutate({
      mutation: UPDATE_CATEGORY,
      variables: {
        categoryId: cat.id,
        name: cat.name
      }
    })
  };

  static deleteCategory = (categoryId: string) => {
    console.log('Delete Category')
    return Vue.prototype.$apollo.mutate({
      mutation: DELETE_CATEGORY,
      variables: {
        categoryId
      }
    })
  };

  static getCategory = (categoryId: string | null) => {
    console.log('get Category')
    return Vue.prototype.$apollo.query({
      query: GET_CATEGORY_BY_ID,
      variables: {
        categoryId
      }
    })
  };

  static getCategories = (input: any) => {
    console.log('get Categories')
    return Vue.prototype.$apollo.query({
      query: GET_CATEGORIES,
      variables: {
        input
      }
    })
  };
}
