import {
  CREATE_TEXT,
  DELETE_TEXT,
  GET_TEXT_BY_ID,
  GET_TEXTS,
  UPDATE_TEXT,
  getTextVariable
} from '@/api/graphql/text'
import apolloClient from '@/utils/vue-apollo'
import {ICategoryData, ITextData} from '@/api/types'

export const defaultTextData: ITextData = {
  id: null,
  name: '',
  content: null
}

export default class ApiText {
  static createText = (name: string, content: string, categories: ICategoryData[]) => {
    console.log('Create Text')
    return apolloClient.mutate({
      mutation: CREATE_TEXT,
      variables: {
        name,
        content,
        categories
      }
    })
  };

  static updateText = (textId: string, name: string, content: string, categories: ICategoryData[]) => {
    console.log('Update Text')
    return apolloClient.mutate({
      mutation: UPDATE_TEXT,
      variables: {
        textId,
        name,
        content,
        categories
      }
    })
  };

  static deleteText = (textId: string) => {
    console.log('Delete Text')
    return apolloClient.mutate({
      mutation: DELETE_TEXT,
      variables: {
        textId
      }
    })
  };

  static getText = (textId: string | null) => {
    console.log('get Text')
    return apolloClient.query({
      query: GET_TEXT_BY_ID,
      variables: {
        textId
      }
    })
  };

  static getTexts = (input: any) => {
    console.log('get Texts')
    return apolloClient.query({
      query: GET_TEXTS,
      variables: {
        input
      }
    })
  };
}
