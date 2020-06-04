/* eslint-disable */
export interface ITemplateData {
  id: string | null
  name: string
  content: string | null
}

export interface ICatalogData {
  id: string | null
  name: string
  content: string | null
}

export interface ITextData {
  id: string | null
  name: string
  content: string | null
}

export interface IPatternData {
  id: string | null
  name: string
  content: string | null
}

export interface IBackgroundData {
  id: string | null
  name: string
  content: string | null
}

export interface IImageData {
  id: string | null
  name: string
  content: string | null
}


export interface ICategoryData {
  id: string | null
  name: string
  parent: string | null
}


export interface IArticleData {
  id: number
  status: string
  title: string
  abstractContent: string
  fullContent: string
  sourceURL: string
  imageURL: string
  timestamp: string | number
  platforms: string[]
  disableComment: boolean
  importance: number
  author: string
  reviewer: string
  type: string
  pageviews: number
}

export interface IRoleData {
  key: string
  name: string
  description: string
  routes: any
}

export interface ITransactionData {
  orderId: string
  timestamp: string | number
  username: string
  price: number
  status: string
}

export interface IUserData {
  id: number
  username: string
  password: string
  name: string
  email: string
  phone: string
  avatar: string
  introduction: string
  roles: string[]
}
