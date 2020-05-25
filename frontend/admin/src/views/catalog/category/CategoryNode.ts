
export default class CategoryNode {
  original: any
  id: string
  name: string
  children: [CategoryNode]

  dragDisabled = true
  addTreeNodeDisabled = true
  addLeafNodeDisabled = true
  editNodeDisabled = true
  delNodeDisabled = true
  constructor(cate: any) {
    this.original = cate
    this.id = cate.id
    this.name = cate.name
    this.children = cate.children.map((child: any) => new CategoryNode(child))
  }
}
