/* eslint-disable */
import {
  GET_BACKGROUND_BY_ID,
  GET_BACKGROUNDS
} from '@/api/graphql/query/background'
import { IBackgroundData, ICategoryData } from '@/api/types';
import Vue from 'vue';

export default class GraphqlApiBackground {

  private static listQuery = {
    start: 1,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  static getBackground = (backgroundId: string | null) => {
    console.log('get Background')
    return Vue.prototype.$apollo.query({
      query: GET_BACKGROUND_BY_ID,
      variables: {
        backgroundId
      }
    })
  };

  static getBackgrounds = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Backgrounds')

    const query = JSON.parse(JSON.stringify(GraphqlApiBackground.listQuery))
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
      query: GET_BACKGROUNDS,
      variables: {
        query
      }
    })
  };
}
