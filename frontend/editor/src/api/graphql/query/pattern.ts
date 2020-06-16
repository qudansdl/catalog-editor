import gql from 'graphql-tag'
import Mustache from 'mustache'


export const GET_PATTERNS = gql`query($input: DataTablesInput) {
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

export const GET_PATTERN_BY_ID = gql`query($patternId: UUID) {
  pattern(patternId: $patternId) {
    id
    name
    content
    created
    updated
  }
}`
