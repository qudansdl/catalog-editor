/* eslint-disable */

import { ICategoryData, IImageData } from '@/api/types';
import Vue from 'vue';


export default class RestApiImage {

  static getImage = (imageId: string | null) => {
    console.log('get Image')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getImages = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Images')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };
}
