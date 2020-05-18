import { CREATE_IMAGE, GET_IMAGES, getImageVariable } from '@/api/graphql/image';
import Vue from 'vue';

export default class ApiImage {
  static uploadImages = (file: any) => {
    console.log('Upload Images');
    return Vue.prototype.$apollo.mutate({
      mutation: CREATE_IMAGE,
      variables: {
        file,
      },
      context: {
        hasUpload: true, // Important!
      },
    });
  };

  static getImages = (category: string, pageSize: number, currentPage: number) => {
    console.log('get images');
    return Vue.prototype.$apollo.query({
      query: GET_IMAGES,
      variables: getImageVariable(category, pageSize, currentPage),
    });
  };
}
