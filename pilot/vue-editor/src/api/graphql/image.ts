import gql from 'graphql-tag';
import Mustache from "mustache";

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
        recordsTotal
        recordsFiltered
        error
        data {
            id
            content
        }
    }
}`;

export function getImageVariable(category: string, pageSize: number, currentPage: number){
    let variableTemplate =  `{
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
    }`;

    let start = (currentPage-1) * pageSize
    let variables = Mustache.render(variableTemplate, {category, pageSize, start})
    return JSON.parse(variables)
}
