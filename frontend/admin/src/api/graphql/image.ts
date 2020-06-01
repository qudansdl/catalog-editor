import gql from 'graphql-tag'
import Mustache from 'mustache'

export const CREATE_IMAGE = gql`
    mutation CreateImage($name: String, $content: String!, $categories: [CategoryInput]){
        createImage(name: $name, content: $content, categories: $categories) {
            id,
            name,
            content
        }
    }
`

export const UPDATE_IMAGE = gql`
  mutation updateImage($imageId: UUID!, $name: String, $content: String!, $categories: [CategoryInput]){
    updateImage(imageId: $imageId, name: $name, content: $content, categories: $categories) {
      id
      name
      content
      created
      updated
    }
  }
`

export const DELETE_IMAGE = gql`
  mutation deleteImage($imageId: UUID!){
    deleteImage(imageId: $imageId)
  }
`

export const GET_IMAGES = gql`query($input: DataTablesInput) {
    images(input: $input) {
        recordsTotal
        recordsFiltered
        error
        data {
            id
            name
            content
            created
            updated
        }
    }
}`

export const GET_IMAGE_BY_ID = gql`query($imageId: UUID) {
  image(imageId: $imageId) {
    id
    name
    content
    created
    updated
  }
}`

export function getImageVariable(category: string, pageSize: number, currentPage: number) {
  const variableTemplate = `{
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
  const variables = Mustache.render(variableTemplate, { category, pageSize, start })
  return JSON.parse(variables)
}
