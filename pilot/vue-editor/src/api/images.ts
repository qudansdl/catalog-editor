import { client }  from '@/plugins/apolloClient'
import {CREATE_IMAGE, GET_IMAGES, getImageVariable} from "@/api/graphql/image";

export const getImages = (category: string, pageSize: number, currentPage: number) =>
    client.query({
        query: GET_IMAGES,
        variables: getImageVariable(category, pageSize, currentPage)
    })


export const uploadImages = (file: any) =>
    client.mutate({
        mutation: CREATE_IMAGE,
        variables: {
            file: file
        },
        context: {
            hasUpload: true // Important!
        }
    })
