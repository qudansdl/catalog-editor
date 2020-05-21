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

  fun category(categoryId: String?): Category {
    return if(categoryId == null)
    {
      val ROOT_CATEGORY = "34589c99-97b0-48c9-8bbb-be663839a12b"
      categoryService.getCategoryById(ROOT_CATEGORY).orElse(null)
    }else{
      categoryService.getCategoryById(categoryId).orElse(null)
    }
  }
}
