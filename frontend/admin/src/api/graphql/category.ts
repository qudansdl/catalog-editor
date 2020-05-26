import gql from 'graphql-tag'

export const CREATE_CATEGORY = gql`
  mutation CreateCategory($name: String){
    createCategory(file: name) {
      name
    }
  }
`

export const GET_CATEGORIES = gql`query($input: DataTablesInput) {
    categories(input: $input) {
        data {
            id
            name
            created
            updated
        }
    }
}`

export const GET_CATEGORY_BY_ID = gql`query($categoryId: UUID) {
  category(categoryId: $categoryId) {
    id
    name
    created
    updated
    children {
      id
      name
      created
      updated
      children {
        id
        name
        created
        updated
        children {
          id
          name
          created
          updated
          children {
            id
            name
            created
            updated
          }
        }
      }
    }

  }
}`
