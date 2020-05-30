import gql from 'graphql-tag'
import Mustache from 'mustache'

export const CREATE_IMAGE = gql`
    mutation CreatePattern($name: String, $content: String!){
        createPattern(name: $name, content: $content) {
            id,
            name,
            content
        }
    }
`

export const UPDATE_IMAGE = gql`
  mutation updatePattern($patternId: UUID!, $name: String, $content: String!){
    updatePattern(patternId: $patternId, name: $name, content: $content) {
      id
      name
      content
      created
      updated
    }
  }
`

export const DELETE_IMAGE = gql`
  mutation deletePattern($patternId: UUID!){
    deletePattern(patternId: $patternId)
  }
`

export const GET_IMAGES = gql`query($input: DataTablesInput) {
    patterns(input: $input) {
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

export const GET_IMAGE_BY_ID = gql`query($patternId: UUID) {
  pattern(patternId: $patternId) {
    id
    name
    content
    created
    updated
  }
}`

export function getPatternVariable(category: string, pageSize: number, currentPage: number) {
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
