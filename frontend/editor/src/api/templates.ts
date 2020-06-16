/* eslint-disable */
import { ICategoryData, ITemplateData } from '@/api/types';
import RestApiTemplate from '@/api/rest/templates';
import GraphqlApiTemplate from '@/api/graphql/templates';

export default class ApiTemplate {
  static createTemplate = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Template')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiTemplate.createTemplate(
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }else{
      return GraphqlApiTemplate.createTemplate(
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }
  };

  static updateTemplate = (
    templateId: string,
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Update Template')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiTemplate.updateTemplate(
        templateId,
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }else{
      return GraphqlApiTemplate.updateTemplate(
        templateId,
        name,
        content,
        image,
        thumbnail,
        categories
      )
    }
  };


  static getTemplate = async (templateId: string): Promise<any> => {
    console.log('get Template : ' + templateId)
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const {data} = await RestApiTemplate.getTemplate(
        templateId
      )
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiTemplate.getTemplate(
        templateId
      )
      return new Promise((resolve) => {
        resolve(
          {
            data: data.template
          }
        );
      });
    }
  };

  static getTemplates = async (start: number, length: number): Promise<any> => {
    console.log('get Templates')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      const { data } = await RestApiTemplate.getTemplates(
        start, length
      )
      return new Promise((resolve) => {
        resolve(data);
      });
    }else{
      const { data } = await GraphqlApiTemplate.getTemplates(
        start, length
      )
      return new Promise((resolve) => {
        resolve(
          {
            data: {
              total: data.templates.recordsFiltered,
              list: data.templates.data
            }
          }
        );
      });
    }
  };
}
