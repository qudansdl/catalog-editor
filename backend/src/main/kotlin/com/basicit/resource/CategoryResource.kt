package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Category
import com.basicit.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/api/categories")
class CategoryResource(private val categoryService: CategoryService) {

  @GetMapping
  fun getCategories(input: DataTablesInput): DataTablesOutput<Category> =
          categoryService.getCategories(input)

  @PostMapping
  fun addCategory(@Valid @RequestBody category: Category): ResponseEntity<Category> =
    ResponseEntity.ok(categoryService.addCategory(category))

  @GetMapping("/{id}")
  fun getCategoryById(@PathVariable(value = "id") categoryId: String): ResponseEntity<Category> =
    categoryService.getCategoryById(categoryId).map { category ->
      ResponseEntity.ok(category)
    }.orElse(ResponseEntity.notFound().build())

  @PutMapping("/{id}")
  fun updateCategoryById(
    @PathVariable(value = "id") categoryId: String,
    @Valid @RequestBody name: String): ResponseEntity<Category> =
    categoryService.putCategory(categoryId, name)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") categoryId: String): ResponseEntity<Void> =
    if (categoryService.deleteCategory(categoryId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
