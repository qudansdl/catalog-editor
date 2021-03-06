/* eslint-disable */
import gql from 'graphql-tag'
import Mustache from 'mustache'

export const CREATE_TEMPLATE = gql`
    mutation CreateTemplate(
      $name: String,
      $content: String!,
      $image: String!,
      $thumbnail: String!,
      $categories: [CategoryInput]
    ){
        createTemplate(
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

export const UPDATE_TEMPLATE = gql`
  mutation updateTemplate(
    $templateId: UUID!,
    $name: String,
    $content: String!,
    $image: String!,
    $thumbnail: String!,
    $categories: [CategoryInput]
  ){
    updateTemplate(
      templateId: $templateId,
      name: $name,
      content: $content,
      image: $image,
      thumbnail: $thumbnail,
      categories: $categories
    ) {
      id
      name
      created
      updated
    }
  }
`


export const GET_TEMPLATES = gql`query($input: DataTablesInput) {
    templates(input: $input) {
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

export const GET_TEMPLATE_BY_ID = gql`query($templateId: UUID) {
  template(templateId: $templateId) {
    id
    name
    content
    image
    thumbnail
    created
    updated
  }
}`
