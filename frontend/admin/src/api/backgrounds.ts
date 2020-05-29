import {
  CREATE_BACKGROUND,
  DELETE_BACKGROUND,
  GET_BACKGROUND_BY_ID,
  GET_BACKGROUNDS,
  UPDATE_BACKGROUND,
  getBackgroundVariable
} from '@/api/graphql/background'
import apolloClient from '@/utils/vue-apollo'
import { IBackgroundData } from '@/api/types'

export const defaultBackgroundData: IBackgroundData = {
  id: null,
  name: '',
  content: null
}

export default class ApiBackground {
  static createBackground = (name: string, content: string) => {
    console.log('Create Background')
    return apolloClient.mutate({
      mutation: CREATE_BACKGROUND,
      variables: {
        name,
        content
      }
    })
  };

  static updateBackground = (backgroundId: string, name: string, content: string) => {
    console.log('Update Background')
    return apolloClient.mutate({
      mutation: UPDATE_BACKGROUND,
      variables: {
        backgroundId,
        name,
        content
      }
    })
  };

  static deleteBackground = (backgroundId: string) => {
    console.log('Delete Background')
    return apolloClient.mutate({
      mutation: DELETE_BACKGROUND,
      variables: {
        backgroundId
      }
    })
  };

  static getBackground = (backgroundId: string | null) => {
    console.log('get Background')
    return apolloClient.query({
      query: GET_BACKGROUND_BY_ID,
      variables: {
        backgroundId
      }
    })
  };

  static getBackgrounds = (input: any) => {
    console.log('get Backgrounds')
    return apolloClient.query({
      query: GET_BACKGROUNDS,
      variables: {
        input
      }
    })
  };
}
