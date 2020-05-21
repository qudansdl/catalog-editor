package com.basicit.graphql.resolver

import com.basicit.model.Category
import com.basicit.service.CategoryService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class CategoryMutationResolver (private val categoryService: CategoryService) : GraphQLMutationResolver {
  fun createCategory(name: String, parentId: String?): Category {

    val parent = parentId?.let { categoryService.getCategoryById(parentId) }?.orElseGet { (null) }

    return categoryService.addCategory(
            Category(name = name, parent = parent, children = null)
    )
  }

  fun updateCategory(categoryId: String, name: String): Category {
    return categoryService.putCategory(categoryId, name).orElse(null)
  }

  fun deleteCategory(categoryId: String): Boolean = categoryService.deleteCategory(categoryId)
}
