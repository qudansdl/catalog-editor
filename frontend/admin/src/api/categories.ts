import {
  CREATE_CATEGORY,
  GET_CATEGORIES,
  GET_CATEGORY_BY_ID
} from '@/api/graphql/category'
import apolloClient from '@/utils/vue-apollo'
import { ICategoryData } from "@/api/types";

export const defaultCategoryData: ICategoryData = {
  id: null,
  name: ''
}


export default class ApiCategory {
  static createCategory = (name: string) => {
    console.log('Create Category')
    return apolloClient.mutate({
      mutation: CREATE_CATEGORY,
      variables: {
        name
      }
    })
  };

  static getCategory = (id: string | null) => {
    console.log('get Category')
    return apolloClient.query({
      query: GET_CATEGORY_BY_ID,
      variables: {
        id
      }
    })
  };

  static getCategories = (input: any) => {
    console.log('get Categories')
    return apolloClient.query({
      query: GET_CATEGORIES,
      variables: {
        input
      }
    })
  };
}