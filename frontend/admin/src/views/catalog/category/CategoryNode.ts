
export default class CategoryNode {
  original: any
  id: string
  name: string
  children: [CategoryNode]

  dragDisabled = false
  addTreeNodeDisabled = false
  addLeafNodeDisabled = false
  editNodeDisabled = false
  delNodeDisabled = false
  constructor(cate: any) {
    this.original = cate
    this.id = cate.id
    this.name = cate.name
    this.children = cate.children.map((child: any) => new CategoryNode(child))
  }
}
