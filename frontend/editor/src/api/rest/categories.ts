/* eslint-disable */
import { ICategoryData } from '@/api/types'
import Vue from 'vue';

export default class RestApiCategory {

  static getCategory = (categoryId: string | null) => {
    console.log('get Category')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getCategories = (input: any) => {
    console.log('get Categories')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };
}
