/* eslint-disable */
import { IBackgroundData, ICategoryData } from '@/api/types';
import RestApiBackground from '@/api/rest/backgrounds';
import GraphqlApiBackground from '@/api/graphql/backgrounds';

export default class ApiBackground {

  static getBackground = async (backgroundId: string | null) => {
    console.log('get Background')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiBackground.getBackground(backgroundId)
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiBackground.getBackground(backgroundId)
      return new Promise((resolve) => {
        resolve(
          {
            data: data.background
          }
        );
      });
    }
  };

  static getBackgrounds = async (start: number, length: number, categories: ICategoryData[]) : Promise<any> => {
    console.log('get Backgrounds')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiBackground.getBackgrounds(start, length, categories)
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiBackground.getBackgrounds(start, length, categories)
      return new Promise((resolve) => {
        resolve(
          {
            data: {
              total: data.backgrounds.recordsFiltered,
              list: data.backgrounds.data
            }
          }
        );
      });
    }
  };
}
