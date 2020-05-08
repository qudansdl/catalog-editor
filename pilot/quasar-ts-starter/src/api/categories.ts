import { GET_CATEGORIES } from '@/api/graphql/category';
import { Vue } from 'vue/types/vue';

class Category {
  getCategories = () => Vue.prototype.$apollo.query({
    query: GET_CATEGORIES,
  });
}
