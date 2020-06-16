/* eslint-disable */
import { ICategoryData, ICatalogData } from '@/api/types';
import RestApiBackground from '@/api/rest/backgrounds';
import GraphqlApiBackground from '@/api/graphql/backgrounds';
import RestApiCatalog from '@/api/rest/catalogs';
import GraphqlApiCatalog from '@/api/graphql/catalogs';

export default class ApiCatalog {
  static createCatalog = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Catalog')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiCatalog.createCatalog(
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }else{
      return GraphqlApiCatalog.createCatalog(
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }
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
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiCatalog.updateCatalog(
        catalogId,
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }else{
      return GraphqlApiCatalog.updateCatalog(
        catalogId,
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }
  };


  static getCatalog = (catalogId: string) => {
    console.log('get Catalog : ' + catalogId)
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiCatalog.getCatalog(
        catalogId
      )
    }else{
      return GraphqlApiCatalog.getCatalog(
        catalogId
      )
    }
  };

  static getCatalogs = (start: number, length: number) => {
    console.log('get Catalogs')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiCatalog.getCatalogs(
        start, length
      )
    }else{
      return GraphqlApiCatalog.getCatalogs(
        start, length
      )
    }
  };
}
