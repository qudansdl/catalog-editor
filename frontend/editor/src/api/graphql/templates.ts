/* eslint-disable */
import {
  CREATE_TEMPLATE,
  GET_TEMPLATE_BY_ID,
  GET_TEMPLATES,
  UPDATE_TEMPLATE
} from '@/api/graphql/query/templates'
import { ICategoryData, ITemplateData } from '@/api/types';
import Vue from 'vue';
import { CREATE_CATALOG, GET_CATALOG_BY_ID, GET_CATALOGS, UPDATE_CATALOG } from '@/api/graphql/query/catalogs';

export default class GraphqlApiTemplate {


  private static listQuery = {
    start: 0,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }


  static createTemplate = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Catalog')
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_TEMPLATE,
      variables: {
        name,
        content,
        image,
        thumbnail,
        categories
      }
    })
  };

  static updateTemplate = (
    catalogId: string,
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Update Catalog')
    return Vue.prototype.$apollo.mutate({
      mutation: UPDATE_TEMPLATE,
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

  static getTemplate = (catalogId: string) => {
    console.log('get Catalog : ' + catalogId)
    return Vue.prototype.$apollo.query({
      query: GET_TEMPLATE_BY_ID,
      variables: {
        catalogId
      }
    })
  };

  static getTemplates = (start: number, length: number) => {
    console.log('get Catalogs')

    const query = JSON.parse(JSON.stringify(GraphqlApiTemplate.listQuery))
    query.start = start
    query.length = length
    return Vue.prototype.$apollo.query({
      query: GET_TEMPLATES,
      variables: {
        query
      }
    })
  };

}
