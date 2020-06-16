/* eslint-disable */
import gql from 'graphql-tag'
import Mustache from 'mustache'

export const CREATE_CATALOG = gql`
    mutation CreateCatalog(
      $name: String,
      $content: String!,
      $image: String!,
      $thumbnail: String!,
      $categories: [CategoryInput]
    ){
        createCatalog(
          name: $name,
          content: $content,
          image: $image,
          thumbnail: $thumbnail,
          categories: $categories
        ) {
            id,
            name
        }
    }
`

export const UPDATE_CATALOG = gql`
  mutation updateCatalog(
    $catalogId: UUID!,
    $name: String,
    $content: String!,
    $image: String!,
    $thumbnail: String!,
    $categories: [CategoryInput]
  ){
    updateCatalog(
      catalogId: $catalogId,
      name: $name,
      content: $content,
      image: $image,
      thumbnail: $thumbnail,
      categories: $categories
    ) {
      id
      name
      content
      created
      updated
    }
  }
`

export const GET_CATALOGS = gql`query($input: DataTablesInput) {
    catalogs(input: $input) {
        recordsTotal
        recordsFiltered
        error
        data {
            id
            name
            thumbnail
            created
            updated
        }
    }
}`

export const GET_CATALOG_BY_ID = gql`query($catalogId: UUID!) {
  catalog(catalogId: $catalogId) {
    id
    name
    content
    image
    thumbnail
    created
    updated
  }
}`

