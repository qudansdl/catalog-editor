/* eslint-disable */
import gql from 'graphql-tag'

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
