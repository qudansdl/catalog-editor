import Vue from 'vue'
import VueApollo from 'vue-apollo'


import { ApolloClient } from 'apollo-client'
import { createUploadLink } from 'apollo-upload-client'
import { InMemoryCache } from 'apollo-cache-inmemory'

Vue.use(VueApollo)

// Cache implementation
const cache = new InMemoryCache()

// Create the apollo client
const apolloClient = new ApolloClient({
    link: createUploadLink({
        uri: 'http://localhost:8081/graphql'
    }),
    cache
})


const apolloProvider = new VueApollo({
    defaultClient: apolloClient,
})


export default  apolloProvider