/* eslint-disable */
import {
  GET_IMAGE_BY_ID,
  GET_IMAGES
} from '@/api/graphql/query/image'
import { ICategoryData, IImageData } from '@/api/types';
import Vue from 'vue';

export default class GraphqlApiImage {

  private static listQuery = {
    start: 1,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  static getImage = (imageId: string | null) => {
    console.log('get Image')
    return Vue.prototype.$apollo.query({
      query: GET_IMAGE_BY_ID,
      variables: {
        imageId
      }
    })
  };

  static getImages = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Images')
    const query = JSON.parse(JSON.stringify(GraphqlApiImage.listQuery))
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
      query: GET_IMAGES,
      variables: {
        query
      }
    })
  };
}
