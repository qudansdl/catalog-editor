
import {ICategoryData, IPatternData} from '@/api/types'
import Vue from 'vue';

export default class RestApiPattern {

  static getPattern = (patternId: string | null) => {
    console.log('get Pattern')
    return Vue.prototype.$axios.get(`/catalog/patterns/${patternId}`)
  };

  static getPatterns = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Patterns')
    return Vue.prototype.$axios.get(`/catalog/patterns`, {
      params: {
        start,
        length,
        categories: tags.map(c => c.id)
      }
    })
  };
}
