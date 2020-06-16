/* eslint-disable */
import { ICategoryData, ITemplateData } from '@/api/types';
import Vue from 'vue';

export default class RestApiTemplate {
  static createTemplate = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Template')
    Vue.prototype.$axios.post('/catalog/templates.do', {
      name,
      content,
      image,
      thumbnail,
      categories
    })
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
    Vue.prototype.$axios.put('/catalog/templates.do', {
      templateId,
      name,
      content,
      image,
      thumbnail,
      categories
    })
  };

  static getTemplate = (templateId: string) => {
    console.log('get Template')
    return Vue.prototype.$axios.get(`/catalog/templates/${templateId}.do`)
  };

  static getTemplates = (start: number, length: number) => {
    console.log('get Templates')
    return Vue.prototype.$axios.get(`/catalog/templates.do`, {
      params: {
        start,
        length
      }
    })
  };
}
