/* eslint-disable */
import gql from 'graphql-tag'
import Mustache from 'mustache'

export const GET_IMAGES = gql`query($input: DataTablesInput) {
    images(input: $input) {
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

export const GET_IMAGE_BY_ID = gql`query($imageId: UUID) {
  image(imageId: $imageId) {
    id
    name
    content
    created
    updated
  }
}`
