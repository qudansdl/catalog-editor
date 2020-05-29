import gql from 'graphql-tag'
import Mustache from 'mustache'

export const CREATE_BACKGROUND = gql`
    mutation CreateBackground($name: String, $content: String!){
        createBackground(name: $name, content: $content) {
            id,
            name,
            content
        }
    }
`

export const UPDATE_BACKGROUND = gql`
  mutation updateBackground($backgroundId: UUID!, $name: String, $content: String!){
    updateBackground(backgroundId: $backgroundId, name: $name, content: $content) {
      id
      name
      content
      created
      updated
    }
  }
`

export const DELETE_BACKGROUND = gql`
  mutation deleteBackground($backgroundId: UUID!){
    deleteBackground(backgroundId: $backgroundId)
  }
`

export const GET_BACKGROUNDS = gql`query($input: DataTablesInput) {
    backgrounds(input: $input) {
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

export const GET_BACKGROUND_BY_ID = gql`query($backgroundId: UUID) {
  background(backgroundId: $backgroundId) {
    id
    name
    content
    created
    updated
  }
}`

export function getBackgroundVariable(category: string, pageSize: number, currentPage: number) {
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
