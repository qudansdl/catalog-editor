import {
  CREATE_IMAGE,
  DELETE_IMAGE,
  GET_IMAGE_BY_ID,
  GET_IMAGES,
  UPDATE_IMAGE,
  getPatternVariable
} from '@/api/graphql/pattern'
import {ICategoryData, IPatternData} from '@/api/types'
import Vue from 'vue';

export const defaultPatternData: IPatternData = {
  id: null,
  name: '',
  content: null
}

export default class ApiPattern {
  static createPattern = (name: string, content: string, categories: ICategoryData[]) => {
    console.log('Create Pattern')
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_IMAGE,
      variables: {
        name,
        content,
        categories
      }
    })
  };

  static updatePattern = (patternId: string, name: string, content: string, categories: ICategoryData[]) => {
    console.log('Update Pattern')
    return Vue.prototype.$apollo.mutate({
      mutation: UPDATE_IMAGE,
      variables: {
        patternId,
        name,
        content,
        categories
      }
    })
  };

  static deletePattern = (patternId: string) => {
    console.log('Delete Pattern')
    return Vue.prototype.$apollo.mutate({
      mutation: DELETE_IMAGE,
      variables: {
        patternId
      }
    })
  };

  static getPattern = (patternId: string | null) => {
    console.log('get Pattern')
    return Vue.prototype.$apollo.query({
      query: GET_IMAGE_BY_ID,
      variables: {
        patternId
      }
    })
  };

  static getPatterns = (input: any) => {
    console.log('get Patterns')
    return Vue.prototype.$apollo.query({
      query: GET_IMAGES,
      variables: {
        input
      }
    })
  };
}
