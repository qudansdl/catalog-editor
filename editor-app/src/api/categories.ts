import { client }  from '@/plugins/apolloClient';
import { GET_CATEGORIES } from "@/api/graphql/category";

export const getCategories = () =>
    client.query({
        query: GET_CATEGORIES
    });
