/* eslint-disable */
import {
  GET_TEXT_BY_ID,
  GET_TEXTS
} from '@/api/graphql/query/text'
import { ICategoryData, ITextData } from '@/api/types';
import Vue from 'vue';


export default class RestApiText {
  static getText = (textId: string | null) => {
    console.log('get Text')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getTexts = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Texts')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };
}
