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
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
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
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getCatalog = (catalogId: string) => {
    console.log('get Catalog : ' + catalogId)
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getCatalogs = (start: number, length: number) => {
    console.log('get Catalogs')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };
}
