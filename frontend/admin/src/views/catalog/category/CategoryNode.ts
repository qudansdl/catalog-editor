
export default class CategoryNode {
  original: any

  id: String
  name: String
  children: [CategoryNode]

  dragDisabled: boolean =  true
  addTreeNodeDisabled: boolean =  true
  addLeafNodeDisabled: boolean =  true
  editNodeDisabled: boolean =  true
  delNodeDisabled: boolean =  true
  constructor(cate: any) {
    this.original = cate;
    this.id = cate.id;
    this.name = cate.name;
    this.children = cate.children.map((child: any) => new CategoryNode(child));
  }
}
