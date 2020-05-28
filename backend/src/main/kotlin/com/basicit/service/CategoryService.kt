package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Category
import com.basicit.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {
  fun getCategories(input: DataTablesInput?): DataTablesOutput<Category> = categoryRepository.findAll(input)

  fun addCategory(category: Category): Category = categoryRepository.save(category)

  fun getCategoryById(categoryId: UUID): Optional<Category> = categoryRepository.findById(categoryId)

  fun putCategory(
      categoryId: UUID,
      name: String
  ): Optional<Category> =
      categoryRepository.findById(categoryId).map { currentCategory ->
          currentCategory.name = name

         categoryRepository.save(currentCategory)
      }

  fun deleteCategory(categoryId: UUID): Boolean = categoryRepository.findById(categoryId).map { category -> categoryRepository.delete(category)
      true
    }.orElse(false)
}
