<template>
  <div class="app-container">
    <vue-tree-list
      @click="onClick"
      @change-name="onChangeName"
      @delete-node="onDel"
      @add-node="onAddNode"
      :model="data"
      default-tree-node-name="new node"
      default-leaf-node-name="new leaf"
      v-bind:default-expanded="false">
      <span class="icon" slot="addTreeNodeIcon">📂</span>
      <span class="icon" slot="addLeafNodeIcon">＋</span>
      <span class="icon" slot="editNodeIcon">📃</span>
      <span class="icon" slot="delNodeIcon">✂️</span>
      <span class="icon" slot="leafNodeIcon">🍃</span>
      <span class="icon" slot="treeNodeIcon">🌲</span>
    </vue-tree-list>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { VueTreeList, Tree, TreeNode } from 'vue-tree-list'

@Component({
  name: 'Category'
})
export default class extends Vue {
  private data = new Tree([
    {
      name: 'Node 1',
      id: 1,
      pid: 0,
      dragDisabled: true,
      addTreeNodeDisabled: true,
      addLeafNodeDisabled: true,
      editNodeDisabled: true,
      delNodeDisabled: true,
      children: [
        {
          name: 'Node 1-2',
          id: 2,
          isLeaf: true,
          pid: 1
        }
      ]
    },
    {
      name: 'Node 2',
      id: 3,
      pid: 0,
      disabled: true
    },
    {
      name: 'Node 3',
      id: 4,
      pid: 0
    }
  ])
}
</script>
