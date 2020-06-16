/* eslint-disable */
import {
  GET_CATEGORIES,
  GET_CATEGORY_BY_ID
} from '@/api/graphql/query/category'
import { ICategoryData } from '@/api/types'
import Vue from 'vue';



export default class GraphqlApiCategory {
  private static categoryQuery = {
    start: 0,
    length: 0,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  static getCategory = (categoryId: string | null) => {
    console.log('get Category')
    return Vue.prototype.$apollo.query({
      query: GET_CATEGORY_BY_ID,
      variables: {
        categoryId
      }
    })
  };

  static getCategories = (search: string) => {
    const query = JSON.parse(JSON.stringify(GraphqlApiCategory.categoryQuery))
    query.columns.push({
      name: 'name',
      operation: 'like',
      value: search
    })

    console.log('get Categories')
    return Vue.prototype.$apollo.query({
      query: GET_CATEGORIES,
      variables: {
        query
      }
    })
  };
}
