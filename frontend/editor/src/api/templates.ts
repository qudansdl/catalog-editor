/* eslint-disable */
import {
  CREATE_TEMPLATE,
  DELETE_TEMPLATE,
  GET_TEMPLATE_BY_ID,
  GET_TEMPLATES,
  UPDATE_TEMPLATE
} from '@/api/graphql/templates'
import { ICategoryData, ITemplateData } from '@/api/types';
import Vue from 'vue';

export const defaultTemplateData: ITemplateData = {
  id: null,
  name: '',
  content: null
}

export default class ApiTemplate {
  static createTemplate = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Template')
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_TEMPLATE,
      variables: {
        name,
        content,
        image,
        thumbnail,
        categories
      }
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
    return Vue.prototype.$apollo.mutate({
      mutation: UPDATE_TEMPLATE,
      variables: {
        templateId,
        name,
        content,
        image,
        thumbnail,
        categories
      }
    })
  };

  static deleteTemplate = (templateId: string) => {
    console.log('Delete Template')
    return Vue.prototype.$apollo.mutate({
      mutation: DELETE_TEMPLATE,
      variables: {
        templateId
      }
    })
  };

  static getTemplate = (templateId: string) => {
    console.log('get Template')
    return Vue.prototype.$apollo.query({
      query: GET_TEMPLATE_BY_ID,
      variables: {
        templateId
      }
    })
  };

  static getTemplates = (input: any) => {
    console.log('get Templates')
    return Vue.prototype.$apollo.query({
      query: GET_TEMPLATES,
      variables: {
        input
      }
    })
  };
}
