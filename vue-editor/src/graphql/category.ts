import gql from 'graphql-tag';

    export const GET_CATEGORIES = gql`query($input: DataTablesInput) {
        categories(input: $input) {
            data {
                id
                name
            }
        }
    }`;


export const GET_CATEGORY_BY_ID  = gql`
query($categoryId: UUID!) {
  category(categoryId: $categoryId) {
    data {
        name
    }
  }
}`;
