/* eslint-disable */
import {
  CREATE_CATALOG,
  GET_CATALOG_BY_ID,
  GET_CATALOGS,
  UPDATE_CATALOG
} from '@/api/graphql/query/catalogs'
import { ICategoryData, ICatalogData } from '@/api/types';
import Vue from 'vue';

export default class GraphqlApiCatalog {


  private static listQuery = {
    start: 0,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }


  static createCatalog = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Catalog')
    return Vue.prototype.$apollo.mutate({
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
    return Vue.prototype.$apollo.mutate({
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

  static getCatalog = (catalogId: string) => {
    console.log('get Catalog : ' + catalogId)
    return Vue.prototype.$apollo.query({
      query: GET_CATALOG_BY_ID,
      variables: {
        catalogId
      }
    })
  };

  static getCatalogs = (start: number, length: number) => {
    console.log('get Catalogs')

    const query = JSON.parse(JSON.stringify(GraphqlApiCatalog.listQuery))
    query.start = start
    query.length = length
    return Vue.prototype.$apollo.query({
      query: GET_CATALOGS,
      variables: {
        query
      }
    })
  };
}
