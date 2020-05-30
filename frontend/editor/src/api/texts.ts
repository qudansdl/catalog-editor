/* eslint-disable */
import {
  CREATE_TEXT,
  DELETE_TEXT,
  GET_TEXT_BY_ID,
  GET_TEXTS,
  UPDATE_TEXT,
  getTextVariable
} from '@/api/graphql/text'
import { ITextData } from '@/api/types'
import Vue from 'vue';

export const defaultTextData: ITextData = {
  id: null,
  name: '',
  content: null
}

export default class ApiText {
  static createText = (name: string, content: string) => {
    console.log('Create Text')
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_TEXT,
      variables: {
        name,
        content
      }
    })
  };

  static updateText = (textId: string, name: string, content: string) => {
    console.log('Update Text')
    return Vue.prototype.$apollo.mutate({
      mutation: UPDATE_TEXT,
      variables: {
        textId,
        name,
        content
      }
    })
  };

  static deleteText = (textId: string) => {
    console.log('Delete Text')
    return Vue.prototype.$apollo.mutate({
      mutation: DELETE_TEXT,
      variables: {
        textId
      }
    })
  };

  static getText = (textId: string | null) => {
    console.log('get Text')
    return Vue.prototype.$apollo.query({
      query: GET_TEXT_BY_ID,
      variables: {
        textId
      }
    })
  };

  static getTexts = (input: any) => {
    console.log('get Texts')
    return Vue.prototype.$apollo.query({
      query: GET_TEXTS,
      variables: {
        input
      }
    })
  };
}
