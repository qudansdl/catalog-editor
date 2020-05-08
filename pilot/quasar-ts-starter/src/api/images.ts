import { CREATE_IMAGE, GET_IMAGES, getImageVariable } from '@/api/graphql/image';
import { Vue } from 'vue/types/vue';

class Image {
  uploadImages = (file: any) => Vue.prototype.$apollo.mutate({
    mutation: CREATE_IMAGE,
    variables: {
      file,
    },
    context: {
      hasUpload: true, // Important!
    },
  });

  getImages = (category: string, pageSize: number, currentPage: number) => Vue.prototype.$apollo.query({
    query: GET_IMAGES,
    variables: getImageVariable(category, pageSize, currentPage),
  });
}
