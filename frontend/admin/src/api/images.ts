import {
  CREATE_IMAGE,
  DELETE_IMAGE,
  GET_IMAGE_BY_ID,
  GET_IMAGES,
  UPDATE_IMAGE,
  getImageVariable
} from '@/api/graphql/image'
import apolloClient from '@/utils/vue-apollo'
import { ICategoryData, IImageData } from '@/api/types'

export const defaultImageData: IImageData = {
  id: null,
  name: '',
  content: null,
  categories: []
}

export default class ApiImage {
  static createImage = (name: string, content: string, categories: ICategoryData[]) => {
    debugger
    console.log('Create Image : ' + name + ' ' + categories)
    return apolloClient.mutate({
      mutation: CREATE_IMAGE,
      variables: {
        name,
        content,
        categories
      }
    })
  };

  static updateImage = (imageId: string, name: string, content: string, categories: ICategoryData[]) => {
    console.log('Update Image')
    return apolloClient.mutate({
      mutation: UPDATE_IMAGE,
      variables: {
        imageId,
        name,
        content,
        categories
      }
    })
  };

  static deleteImage = (imageId: string) => {
    console.log('Delete Image')
    return apolloClient.mutate({
      mutation: DELETE_IMAGE,
      variables: {
        imageId
      }
    })
  };

  static getImage = (imageId: string | null) => {
    console.log('get Image')
    return apolloClient.query({
      query: GET_IMAGE_BY_ID,
      variables: {
        imageId
      }
    })
  };

  static getImages = (input: any) => {
    console.log('get Images')
    return apolloClient.query({
      query: GET_IMAGES,
      variables: {
        input
      }
    })
  };
}
