package com.basicit.graphql.resolver

import com.basicit.model.Category
import com.basicit.service.CategoryService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoryMutationResolver (private val categoryService: CategoryService) : GraphQLMutationResolver {
  fun createCategory(name: String? = null): Category =
    categoryService.addCategory(
      Category(name = name)
    )

  fun updateCategory(categoryId: String, name: String? = null): Category =
    categoryService.putCategory(
      categoryId,
      Category(id = UUID.fromString(categoryId), name = name)
    ).orElse(null)

  fun deleteCategory(categoryId: String): Boolean = categoryService.deleteCategory(categoryId)
}
