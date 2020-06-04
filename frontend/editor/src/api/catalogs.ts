/* eslint-disable */
import {
  CREATE_CATALOG,
  DELETE_CATALOG,
  GET_CATALOG_BY_ID,
  GET_CATALOGS,
  UPDATE_CATALOG
} from '@/api/graphql/catalogs'
import { ICategoryData, ICatalogData } from '@/api/types';
import Vue from 'vue';

export const defaultCatalogData: ICatalogData = {
  id: null,
  name: '',
  content: null
}

export default class ApiCatalog {
  static createCatalog = (name: string, content: string, categories: ICategoryData[]) => {
    console.log('Create Catalog')
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_CATALOG,
      variables: {
        name,
        content,
        categories
      }
    })
  };

  static updateCatalog = (catalogId: string, name: string, content: string, categories: ICategoryData[]) => {
    console.log('Update Catalog')
    return Vue.prototype.$apollo.mutate({
      mutation: UPDATE_CATALOG,
      variables: {
        catalogId,
        name,
        content,
        categories
      }
    })
  };

  static deleteCatalog = (catalogId: string) => {
    console.log('Delete Catalog')
    return Vue.prototype.$apollo.mutate({
      mutation: DELETE_CATALOG,
      variables: {
        catalogId
      }
    })
  };

  static getCatalog = (catalogId: string | null) => {
    console.log('get Catalog')
    return Vue.prototype.$apollo.query({
      query: GET_CATALOG_BY_ID,
      variables: {
        catalogId
      }
    })
  };

  static getCatalogs = (input: any) => {
    console.log('get Catalogs')
    return Vue.prototype.$apollo.query({
      query: GET_CATALOGS,
      variables: {
        input
      }
    })
  };
}
