package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Category
import com.basicit.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {
  fun getCategories(input: DataTablesInput?): DataTablesOutput<Category> =
          categoryRepository.findAll(input)

  fun addCategory(category: Category): Category = categoryRepository.save(category)

  fun getCategoryById(categoryId: String): Optional<Category> =
          categoryRepository.findById(categoryId)

  fun putCategory(
          categoryId: String,
          newCategory: Category
  ): Optional<Category> =
      categoryRepository.findById(categoryId).map { currentCategory ->
      val updatedCategory = Category(
              currentCategory.id,
              newCategory.name)

          categoryRepository.save(updatedCategory)
    }

  fun deleteCategory(categoryId: String): Boolean =
          categoryRepository.findById(categoryId).map { category ->
              categoryRepository.delete(category)
      true
    }.orElse(false)
}
