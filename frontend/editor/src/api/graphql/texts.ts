/* eslint-disable */
import {
  GET_TEXT_BY_ID,
  GET_TEXTS
} from '@/api/graphql/query/text'
import { ICategoryData, ITextData } from '@/api/types';
import Vue from 'vue';

export default class GraphqlApiText {
  private static listQuery = {
    start: 0,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  static getText = (textId: string | null) => {
    console.log('get Text')
    return Vue.prototype.$apollo.query({
      query: GET_TEXT_BY_ID,
      variables: {
        textId
      }
    })
  };

  static getTexts = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Texts')

    const query = JSON.parse(JSON.stringify(GraphqlApiText.listQuery))
    query.start = start
    query.length = length
    if (tags.length > 0) {
      query.columns.push({
        name: 'categories',
        operation: '',
        value: '',
        columns: [{
          name: 'id',
          operation: 'in',
          value: tags.map(c => c.id).join(',')
        }]
      })
    }
    return Vue.prototype.$apollo.query({
      query: GET_TEXTS,
      variables: {
        query
      }
    })
  };


}
