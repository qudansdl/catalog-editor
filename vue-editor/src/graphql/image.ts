import gql from 'graphql-tag';

export const CREATE_IMAGE = gql`
    mutation CreateImage($file: Upload!){
        createImage(file: $file) {
            id,
            content
        }
    }
`;


export const GET_IMAGES = gql`query($input: DataTablesInput) {
    images(input: $input) {
        data {
            id
            content
        }
    }
}`;
