import { boot } from 'quasar/wrappers';

import { ApolloClient } from 'apollo-client';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { createUploadLink } from 'apollo-upload-client';

import { ApolloQueryResult, OperationVariables } from 'apollo-client/core/types';
import { MutationOptions, QueryOptions } from 'apollo-client/core/watchQueryOptions';
import { FetchResult } from 'apollo-link';

console.log(JSON.stringify(process.env));
const link = createUploadLink({
  uri: process.env.VUE_APP_BASE_API,
  credentials: 'same-origin',
});

const apolloClient = new ApolloClient({
  link,
  cache: new InMemoryCache({
    addTypename: false,
  }),
});

export interface ApolloInstance {
  query<T = any, TVariables = OperationVariables>(options: QueryOptions<TVariables>): Promise<ApolloQueryResult<T>>;
  mutate<T = any, TVariables = OperationVariables>(options: MutationOptions<T, TVariables>): Promise<FetchResult<T>>;
}

declare module 'vue/types/vue' {
  interface Vue {
    $apollo: ApolloInstance;
  }
}

export default boot(({ Vue }) => {
  // eslint-disable-next-line no-param-reassign
  Vue.prototype.$apollo = apolloClient;
});
