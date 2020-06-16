
import {ICategoryData, IPatternData} from '@/api/types'
import RestApiBackground from '@/api/rest/backgrounds';
import GraphqlApiBackground from '@/api/graphql/backgrounds';
import RestApiPattern from '@/api/rest/patterns';
import GraphqlApiPattern from '@/api/graphql/patterns';

export default class ApiPattern {

  static getPattern = async (patternId: string | null) => {
    console.log('get Pattern')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiPattern.getPattern(patternId)
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiPattern.getPattern(patternId)

      return new Promise((resolve) => {
        resolve(
          {
            data: data.pattern
          }
        );
      });
    }
  };

  static getPatterns = async (start: number, length: number, categories: ICategoryData[]) : Promise<any> => {
    console.log('get Patterns')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiPattern.getPatterns(start, length, categories)
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiPattern.getPatterns(start, length, categories)
      return new Promise((resolve) => {
        resolve(
          {
            data: {
              total: data.pastterns.recordsFiltered,
              list: data.patterns.data
            }
          }
        );
      });
    }
  };

}
