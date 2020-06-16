/* eslint-disable */

import { ICategoryData, IImageData } from '@/api/types';
import RestApiCategory from '@/api/rest/categories';
import GraphqlApiCategory from '@/api/graphql/categories';
import RestApiImage from '@/api/rest/images';
import GraphqlApiImage from '@/api/graphql/images';

export default class ApiImage {

  static getImage = (imageId: string | null) => {
    console.log('get Image')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiImage.getImage(
        imageId
      )
    }else{
      return GraphqlApiImage.getImage(
        imageId
      )
    }
  };

  static getImages = (start: number, length: number, categories: ICategoryData[]) => {
    console.log('get Images')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiImage.getImages(
        start, length, categories
      )
    }else{
      return GraphqlApiImage.getImages(
        start, length, categories
      )
    }
  };
}
