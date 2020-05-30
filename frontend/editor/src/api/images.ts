/* eslint-disable */
import {
  CREATE_IMAGE,
  DELETE_IMAGE,
  GET_IMAGE_BY_ID,
  GET_IMAGES,
  UPDATE_IMAGE,
  getImageVariable
} from '@/api/graphql/image'
import { IImageData } from '@/api/types'
import Vue from 'vue';

export const defaultImageData: IImageData = {
  id: null,
  name: '',
  content: null
}

export default class ApiImage {
  static createImage = (name: string, content: string) => {
    console.log('Create Image')
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_IMAGE,
      variables: {
        name,
        content
      }
    })
  };

  static updateImage = (imageId: string, name: string, content: string) => {
    console.log('Update Image')
    return Vue.prototype.$apollo.mutate({
      mutation: UPDATE_IMAGE,
      variables: {
        imageId,
        name,
        content
      }
    })
  };

  static deleteImage = (imageId: string) => {
    console.log('Delete Image')
    return Vue.prototype.$apollo.mutate({
      mutation: DELETE_IMAGE,
      variables: {
        imageId
      }
    })
  };

  static getImage = (imageId: string | null) => {
    console.log('get Image')
    return Vue.prototype.$apollo.query({
      query: GET_IMAGE_BY_ID,
      variables: {
        imageId
      }
    })
  };

  static getImages = (input: any) => {
    console.log('get Images')
    return Vue.prototype.$apollo.query({
      query: GET_IMAGES,
      variables: {
        input
      }
    })
  };
}
