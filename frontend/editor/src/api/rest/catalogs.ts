/* eslint-disable */
import { ICategoryData, ICatalogData } from '@/api/types';
import Vue from 'vue';


export default class RestApiCatalog {
  static createCatalog = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Catalog')
    Vue.prototype.$axios.post('/catalog/catalogs.do', {
      name,
      content,
      image,
      thumbnail,
      categories
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
    Vue.prototype.$axios.put('/catalog/catalogs.do', {
      catalogId,
      name,
      content,
      image,
      thumbnail,
      categories
    })
  };

  static getCatalog = (catalogId: string) => {
    console.log('get Catalog : ' + catalogId)
    return Vue.prototype.$axios.get(`/catalog/catalogs/${catalogId}.do`)
  };

  static getCatalogs = (start: number, length: number) => {
    console.log('get Catalogs')
    return Vue.prototype.$axios.get(`/catalog/catalogs.do`, {
      params: {
        start,
        length
      }
    })
  };
}
