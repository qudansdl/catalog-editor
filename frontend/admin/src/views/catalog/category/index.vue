<template>
  <div class="app-container">
    <vue-tree-list
      :model="data"
      default-tree-node-name="new node"
      default-leaf-node-name="new leaf"
      :default-expanded="false"
      @click="onClick"
      @change-name="onChangeName"
      @delete-node="onDel"
      @add-node="onAddNode"
    >
      <span
        slot="addTreeNodeIcon"
        class="icon"
      >📂</span>
      <span
        slot="addLeafNodeIcon"
        class="icon"
      >＋</span>
      <span
        slot="editNodeIcon"
        class="icon"
      >📃</span>
      <span
        slot="delNodeIcon"
        class="icon"
      >✂️</span>
      <span
        slot="leafNodeIcon"
        class="icon"
      >🍃</span>
      <span
        slot="treeNodeIcon"
        class="icon"
      >🌲</span>
    </vue-tree-list>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { VueTreeList, Tree, TreeNode } from 'vue-tree-list'
import ApiCategory from '@/api/categories'
import CategoryNode from './CategoryNode'

@Component({
  name: 'Category'
})
export default class extends Vue {
  data: any
  newTree: any

  mounted() {
    const root: any = ApiCategory.getCategory(undefined)
    this.data = new Tree(new CategoryNode(root))
  }

  onDel(node: { remove: () => void }) {
    console.log(node)
    node.remove()
  }

  onChangeName(params: any) {
    console.log(params)
  }

  onAddNode(params: any) {
    console.log(params)
  }

  onClick(params: any) {
    console.log(params)
  }

  addNode() {
    const node = new TreeNode({ name: 'new node', isLeaf: false })
    if (!this.data.children) this.data.children = []
    this.data.addChildren(node)
  }

  getNewTree() {
    const vm = this
    function _dfs(oldNode: any) {
      const newNode = {}

      for (const k in oldNode) {
        if (k !== 'children' && k !== 'parent') {
          newNode[k] = oldNode[k]
        }
      }

      if (oldNode.children && oldNode.children.length > 0) {
        newNode.children = []
        for (let i = 0, len = oldNode.children.length; i < len; i++) {
          newNode.children.push(_dfs(oldNode.children[i]))
        }
      }
      return newNode
    }

    vm.newTree = _dfs(vm.data)
  }
}
</script>
