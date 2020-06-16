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
    return Vue.prototype.$axios.get(`/catalog/texts/${textId}.do`)
  };

  static getTexts = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Texts')
    return Vue.prototype.$axios.get(`/catalog/texts.do`, {
      params: {
        start,
        length,
        categories: tags.map(c => c.id)
      }
    })
  };
}
