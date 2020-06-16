/* eslint-disable */

import RestApiCategory from '@/api/rest/categories';
import GraphqlApiCategory from '@/api/graphql/categories';

export default class ApiCategory {
  static getCategory = async (categoryId: string | null) => {
    console.log('get Category')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiCategory.getCategory(
        categoryId
      )
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiCategory.getCategory(
        categoryId
      )
      return new Promise((resolve) => {
        resolve(
          {
            data: data.category
          }
        );
      });
    }
  };

  static getCategories = async (search: string): Promise<any> => {
    console.log('get Categories')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiCategory.getCategories(
        search
      )
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiCategory.getCategories(
        search
      )
      return new Promise((resolve) => {
        resolve(
          {
            data: {
              total: data.categories.recordsFiltered,
              list: data.categories.data
            }
          }
        );
      });
    }
  };
}
