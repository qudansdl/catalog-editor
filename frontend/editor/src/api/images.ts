/* eslint-disable */

import { ICategoryData, IImageData } from '@/api/types';
import RestApiCategory from '@/api/rest/categories';
import GraphqlApiCategory from '@/api/graphql/categories';
import RestApiImage from '@/api/rest/images';
import GraphqlApiImage from '@/api/graphql/images';

export default class ApiImage {

  static getImage = async (imageId: string | null) => {
    console.log('get Image')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiImage.getImage(
        imageId
      )
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiImage.getImage(
        imageId
      )
      return new Promise((resolve) => {
        resolve(
          {
            data: data.image
          }
        );
      });
    }
  };

  static getImages = async (start: number, length: number, categories: ICategoryData[]): Promise<any> => {
    console.log('get Images')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiImage.getImages(
        start, length, categories
      )
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } =  await GraphqlApiImage.getImages(
        start, length, categories
      )
      return new Promise((resolve) => {
        resolve(
          {
            data: {
              total: data.images.recordsFiltered,
              list: data.images.data
            }
          }
        );
      });
    }
  };
}
