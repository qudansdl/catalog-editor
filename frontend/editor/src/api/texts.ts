/* eslint-disable */

import { ICategoryData, ITextData } from '@/api/types';
import RestApiText from '@/api/rest/texts';
import GraphqlApiText from '@/api/graphql/texts';
import ApiImage from '@/api/images';
import ApiTemplate from '@/api/templates';

export default class ApiText {

  static getText = async (textId: string | null) => {
    console.log('get Text')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await  RestApiText.getText(textId)
      return new Promise((resolve) => {
        resolve({data: data});
      });
    }else{
      const { data } = await GraphqlApiText.getText(textId)
      return new Promise((resolve) => {
        resolve(
          {
            data: data.text
          }
        );
      });
    }
  };

  static getTexts =  async (start: number, length: number, categories: ICategoryData[]) : Promise<any> => {
    console.log('get Texts')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiText.getTexts(start, length, categories)
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiText.getTexts(start, length, categories)
      return new Promise((resolve) => {
        resolve(
          {
              data: {
                total: data.texts.recordsFiltered,
                list: data.texts.data
              }
            }
          );
      });
    }
  };
}
