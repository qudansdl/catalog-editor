/* eslint-disable */

import { ICategoryData, IImageData } from '@/api/types';
import Vue from 'vue';


export default class RestApiImage {

  static getImage = (imageId: string | null) => {
    console.log('get Image')
    return Vue.prototype.$axios.get(`/catalog/images/${imageId}.do`)
  };

  static getImages = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Images')
    return Vue.prototype.$axios.get(`/catalog/images.do`, {
      params: {
        start,
        length,
        categories: tags.map(c => c.id)
      }
    })
  };
}
