/* eslint-disable */
import { ICategoryData, ITemplateData } from '@/api/types';
import Vue from 'vue';

export default class RestApiTemplate {
  static createTemplate = (
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Create Template')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static updateTemplate = (
    templateId: string,
    name: string,
    content: string,
    image: string,
    thumbnail: string,
    categories: ICategoryData[]
  ) => {
    console.log('Update Template')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getTemplate = (templateId: string) => {
    console.log('get Template')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };

  static getTemplates = (start: number, length: number) => {
    console.log('get Templates')
    return new Promise((resolve) => {
      resolve({ data: {} });
    });
  };
}
