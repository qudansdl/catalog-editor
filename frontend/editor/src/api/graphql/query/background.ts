/* eslint-disable */
import gql from 'graphql-tag'
import Mustache from 'mustache'


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

