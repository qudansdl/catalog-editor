import { ApolloClient, DefaultOptions } from 'apollo-client'
import { InMemoryCache } from 'apollo-cache-inmemory'
import { createUploadLink } from 'apollo-upload-client'

import { ApolloQueryResult, OperationVariables } from 'apollo-client/core/types'
import { MutationOptions, QueryOptions } from 'apollo-client/core/watchQueryOptions'
import { FetchResult } from 'apollo-link'

console.log(JSON.stringify(process.env))

const defaultOptions: DefaultOptions = {
  watchQuery: {
    fetchPolicy: 'no-cache',
    errorPolicy: 'ignore'
  },
  query: {
    fetchPolicy: 'no-cache',
    errorPolicy: 'all'
  }
}

const link = createUploadLink({
  uri: process.env.VUE_APP_BASE_API,
  credentials: 'same-origin'
})

const cache = new InMemoryCache({
  addTypename: false
})

const apolloClient = new ApolloClient({
  link,
  cache,
  defaultOptions
})

export interface IApolloInstance
{
  query<T = any, TVariables = OperationVariables>(options: QueryOptions<TVariables>): Promise<ApolloQueryResult<T>>
  mutate<T = any, TVariables = OperationVariables>(options: MutationOptions<T, TVariables>): Promise<FetchResult<T>>
}

declare module 'vue/types/vue' {
  // eslint-disable-next-line @typescript-eslint/interface-name-prefix
  interface Vue {
    $apollo: IApolloInstance
  }
}

export default apolloClient
