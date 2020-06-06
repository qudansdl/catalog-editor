import {
  CREATE_TEMPLATE,
  DELETE_TEMPLATE,
  GET_TEMPLATE_BY_ID,
  GET_TEMPLATES,
  UPDATE_TEMPLATE,
  getTemplateVariable
} from '@/api/graphql/template'
import apolloClient from '@/utils/vue-apollo'
import {ICategoryData, ITemplateData } from '@/api/types'

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
    return apolloClient.mutate({
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
    return apolloClient.mutate({
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
    return apolloClient.mutate({
      mutation: DELETE_TEMPLATE,
      variables: {
        templateId
      }
    })
  };

  static getTemplate = (templateId: string | null) => {
    console.log('get Template')
    return apolloClient.query({
      query: GET_TEMPLATE_BY_ID,
      variables: {
        templateId
      }
    })
  };

  static getTemplates = (input: any) => {
    console.log('get Templates')
    return apolloClient.query({
      query: GET_TEMPLATES,
      variables: {
        input
      }
    })
  };
}
