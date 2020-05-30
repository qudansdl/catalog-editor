import { RouteConfig } from 'vue-router'
import Layout from '@/layout/index.vue'

const catalogRoutes: RouteConfig = {
  path: '/catalog',
  component: Layout,
  redirect: '/catalog/category',
  name: 'Catalog',
  meta: {
    title: 'catalog',
    icon: 'documentation'
  },
  children: [
    {
      path: 'category',
      component: () => import(/* webpackChunkName: "catalog" */ '@/views/catalog/category/index.vue'),
      name: 'Category',
      meta: { title: 'category' }
    },
    {
      path: 'image',
      component: () => import(/* webpackChunkName: "catalog" */ '@/views/catalog/image/index.vue'),
      name: 'Image',
      meta: { title: 'image' }
    },
    {
      path: 'text',
      component: () => import(/* webpackChunkName: "catalog" */ '@/views/catalog/text/index.vue'),
      name: 'Text',
      meta: { title: 'text' }
    },
    {
      path: 'background',
      component: () => import(/* webpackChunkName: "catalog" */ '@/views/catalog/background/index.vue'),
      name: 'Background',
      meta: { title: 'background' }
    },
    {
      path: 'pattern',
      component: () => import(/* webpackChunkName: "catalog" */ '@/views/catalog/pattern/index.vue'),
      name: 'Pattern',
      meta: { title: 'pattern' }
    },
    {
      path: 'template',
      component: () => import(/* webpackChunkName: "catalog" */ '@/views/catalog/template/index.vue'),
      name: 'Template',
      meta: { title: 'template' }
    }
  ]
}

export default catalogRoutes
