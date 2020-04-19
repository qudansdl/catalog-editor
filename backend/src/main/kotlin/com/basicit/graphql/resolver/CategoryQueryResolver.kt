package com.basicit.graphql.resolver

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.model.Category
import graphql.kickstart.tools.GraphQLQueryResolver
import com.basicit.service.CategoryService
import org.springframework.stereotype.Component

@Component
class CategoryQueryResolver (private val categoryService: CategoryService) : GraphQLQueryResolver {
  fun categories(input: DataTablesInput?): DataTablesOutput<Category>  =
          categoryService.getCategories(input)

  fun category(categoryId: String): Category = categoryService.getCategoryById(categoryId).orElse(null)
}
