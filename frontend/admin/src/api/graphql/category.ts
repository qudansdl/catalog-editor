import gql from 'graphql-tag'

export const CREATE_CATEGORY = gql`
  mutation CreateCategory($name: String, $parentId: UUID){
    createCategory(name: $name, parentId: $parentId) {
      id
      name
      created
      updated
    }
  }
`

export const UPDATE_CATEGORY = gql`
  mutation updateCategory($categoryId: UUID!, $name: String){
    updateCategory(categoryId: $categoryId, name: $name) {
      id
      name
      created
      updated
    }
  }
`

export const DELETE_CATEGORY = gql`
  mutation deleteCategory($categoryId: UUID!){
    deleteCategory(categoryId: $categoryId)
  }
`

export const GET_CATEGORIES = gql`query($input: DataTablesInput) {
    categories(input: $input) {
        recordsTotal
        recordsFiltered
        error
        data {
            id
            name
            parent {
              id
              name
            }
            children {
              id
              name
            }
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
