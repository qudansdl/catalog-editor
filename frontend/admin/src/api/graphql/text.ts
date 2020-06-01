import gql from 'graphql-tag'
import Mustache from 'mustache'

export const CREATE_TEXT = gql`
    mutation CreateText($name: String, $content: String!, $categories: [CategoryInput]){
        createText(name: $name, content: $content, categories: $categories) {
            id,
            name,
            content
        }
    }
`

export const UPDATE_TEXT = gql`
  mutation updateText($textId: UUID!, $name: String, $content: String!, $categories: [CategoryInput]){
    updateText(textId: $textId, name: $name, content: $content, categories: $categories) {
      id
      name
      content
      created
      updated
    }
  }
`

export const DELETE_TEXT = gql`
  mutation deleteText($textId: UUID!){
    deleteText(textId: $textId)
  }
`

export const GET_TEXTS = gql`query($input: DataTablesInput) {
    texts(input: $input) {
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

export const GET_TEXT_BY_ID = gql`query($textId: UUID) {
  text(textId: $textId) {
    id
    name
    content
    created
    updated
  }
}`

export function getTextVariable(category: string, pageSize: number, currentPage: number) {
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
