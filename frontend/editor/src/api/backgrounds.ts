/* eslint-disable */
import { IBackgroundData, ICategoryData } from '@/api/types';
import RestApiBackground from '@/api/rest/backgrounds';
import GraphqlApiBackground from '@/api/graphql/backgrounds';

export default class ApiBackground {

  static getBackground = (backgroundId: string | null) => {
    console.log('get Background')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiBackground.getBackground(backgroundId)
    }else{
      return GraphqlApiBackground.getBackground(backgroundId)
    }
  };

  static getBackgrounds = (start: number, length: number, categories: ICategoryData[]) => {
    console.log('get Backgrounds')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiBackground.getBackgrounds(start, length, categories)
    }else{
      return GraphqlApiBackground.getBackgrounds(start, length, categories)
    }
  };
}
