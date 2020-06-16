
import {ICategoryData, IPatternData} from '@/api/types'
import RestApiBackground from '@/api/rest/backgrounds';
import GraphqlApiBackground from '@/api/graphql/backgrounds';
import RestApiPattern from '@/api/rest/patterns';
import GraphqlApiPattern from '@/api/graphql/patterns';

export default class ApiPattern {

  static getPattern = (patternId: string | null) => {
    console.log('get Pattern')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiPattern.getPattern(patternId)
    }else{
      return GraphqlApiPattern.getPattern(patternId)
    }
  };

  static getPatterns = (start: number, length: number, categories: ICategoryData[]) => {
    console.log('get Patterns')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiPattern.getPatterns(start, length, categories)
    }else{
      return GraphqlApiPattern.getPatterns(start, length, categories)
    }
  };

}
