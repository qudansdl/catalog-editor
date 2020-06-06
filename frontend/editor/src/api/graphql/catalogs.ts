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

export const DELETE_CATALOG = gql`
  mutation deleteCatalog($catalogId: UUID!){
    deleteCatalog(catalogId: $catalogId)
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
            thumnbail
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

export function getCatalogVariable(category: string, pageSize: number, currentPage: number) {
  const variableCatalog = `{
        "input": {
            "start": {{start}},
            "length": {{pageSize}} {{#category}},
            "columns": [{
                "name": "categories",
                "columns": [{
                    "name": "id",
                    "operation": "eq",
                    "value": "{{category}}"
                }]
            }]
            {{/category}}
        }
    }`

  const start = (currentPage - 1) * pageSize
  const variables = Mustache.render(variableCatalog, { category, pageSize, start })
  return JSON.parse(variables)
}
