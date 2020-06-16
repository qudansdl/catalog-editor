/* eslint-disable */
import { IBackgroundData, ICategoryData } from '@/api/types';
import Vue from 'vue';
import { GET_TEMPLATES } from '@/api/graphql/query/templates';


export default class RestApiBackground {

  static getBackground = (backgroundId: string | null) => {
    console.log('get Background')
    return Vue.prototype.$axios.get({
      query: GET_TEMPLATES,
      variables: {
        query
      }
    })
  };

  static getBackgrounds = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Backgrounds')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };
}
