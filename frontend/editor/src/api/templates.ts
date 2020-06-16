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


  static getTemplate = (templateId: string) => {
    console.log('get Template : ' + templateId)
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiTemplate.getTemplate(
        templateId
      )
    }else{
      return GraphqlApiTemplate.getTemplate(
        templateId
      )
    }
  };

  static getTemplates = (start: number, length: number) => {
    console.log('get Templates')
    if(process.env.VUE_APP_API_TYPE  === 'REST')
    {
      return RestApiTemplate.getTemplates(
        start, length
      )
    }else{
      return GraphqlApiTemplate.getTemplates(
        start, length
      )
    }
  };
}
