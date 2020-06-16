/* eslint-disable */

import { ICategoryData, ITextData } from '@/api/types';
import RestApiText from '@/api/rest/texts';
import GraphqlApiText from '@/api/graphql/texts';

export default class ApiText {

  static getText = (textId: string | null) => {
    console.log('get Text')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiText.getText(textId)
    }else{
      return GraphqlApiText.getText(textId)
    }
  };

  static getTexts =  (start: number, length: number, categories: ICategoryData[]) => {
    console.log('get Texts')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiText.getTexts(start, length, categories)
    }else{
      return GraphqlApiText.getTexts(start, length, categories)
    }
  };
}
