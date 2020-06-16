/* eslint-disable */
import gql from 'graphql-tag'
import Mustache from 'mustache'

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
