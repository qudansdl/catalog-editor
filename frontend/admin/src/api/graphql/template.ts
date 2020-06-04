import gql from 'graphql-tag'
import Mustache from 'mustache'

export const CREATE_TEMPLATE = gql`
    mutation CreateTemplate($name: String, $description: String!, $categories: [CategoryInput], $blocks: [TemplateBlockInput]){
        createTemplate(name: $name, description: $description, categories: $categories, blocks: $blocks) {
            id,
            name,
            description
        }
    }
`

export const UPDATE_TEMPLATE = gql`
  mutation updateTemplate($templateId: UUID!, $name: String, $description: String!, $categories: [CategoryInput],  $blocks: [TemplateBlockInput]){
    updateTemplate(templateId: $templateId, name: $name, description: $description, categories: $categories, blocks: $blocks) {
      id
      name
      description
      created
      updated
    }
  }
`

export const DELETE_TEMPLATE = gql`
  mutation deleteTemplate($templateId: UUID!){
    deleteTemplate(templateId: $templateId)
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
            description
            created
            updated
        }
    }
}`

export const GET_TEMPLATE_BY_ID = gql`query($templateId: UUID) {
  template(templateId: $templateId) {
    id
    name
    description
    created
    updated
  }
}`

export function getTemplateVariable(category: string, pageSize: number, currentPage: number) {
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