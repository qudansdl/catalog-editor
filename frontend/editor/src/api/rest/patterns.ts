
import {ICategoryData, IPatternData} from '@/api/types'
import Vue from 'vue';

export default class RestApiPattern {

  static getPattern = (patternId: string | null) => {
    console.log('get Pattern')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getPatterns = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Patterns')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };
}
