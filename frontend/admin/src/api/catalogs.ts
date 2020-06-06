import {
  CREATE_CATALOG,
  DELETE_CATALOG,
  GET_CATALOG_BY_ID,
  GET_CATALOGS,
  UPDATE_CATALOG,
  getCatalogVariable
} from '@/api/graphql/catalog'
import apolloClient from '@/utils/vue-apollo'
import {ICategoryData, ICatalogData } from '@/api/types'

export const defaultCatalogData: ICatalogData = {
  id: null,
  name: '',
  content: null
}

export default class ApiCatalog {
  static createCatalog = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Catalog')
    return apolloClient.mutate({
      mutation: CREATE_CATALOG,
      variables: {
        name,
        content,
        image,
        thumbnail,
        categories
      }
    })
  };

  static updateCatalog = (
    catalogId: string,
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Update Catalog')
    return apolloClient.mutate({
      mutation: UPDATE_CATALOG,
      variables: {
        catalogId,
        name,
        content,
        image,
        thumbnail,
        categories
      }
    })
  };

  static deleteCatalog = (catalogId: string) => {
    console.log('Delete Catalog')
    return apolloClient.mutate({
      mutation: DELETE_CATALOG,
      variables: {
        catalogId
      }
    })
  };

  static getCatalog = (catalogId: string | null) => {
    console.log('get Catalog')
    return apolloClient.query({
      query: GET_CATALOG_BY_ID,
      variables: {
        catalogId
      }
    })
  };

  static getCatalogs = (input: any) => {
    console.log('get Catalogs')
    return apolloClient.query({
      query: GET_CATALOGS,
      variables: {
        input
      }
    })
  };
}
