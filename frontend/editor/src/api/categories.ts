/* eslint-disable */

import RestApiCategory from '@/api/rest/categories';
import GraphqlApiCategory from '@/api/graphql/categories';

export default class ApiCategory {
  static getCategory = (categoryId: string | null) => {
    console.log('get Category')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiCategory.getCategory(
        categoryId
      )
    }else{
      return GraphqlApiCategory.getCategory(
        categoryId
      )
    }
  };

  static getCategories = (search: string) => {
    console.log('get Categories')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiCategory.getCategories(
        search
      )
    }else{
      return GraphqlApiCategory.getCategories(
        search
      )
    }
  };
}
