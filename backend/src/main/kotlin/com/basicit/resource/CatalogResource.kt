package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.service.CatalogService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/api/catalogs")
class CatalogResource(private val catalogService: CatalogService) {

  @GetMapping
  fun getCatalogs(input: DataTablesInput): DataTablesOutput<Catalog> = catalogService.getCatalogs(input)

  @PostMapping
  fun addCatalog(@Valid @RequestBody catalog: Catalog): ResponseEntity<Catalog> =
    ResponseEntity.ok(catalogService.addCatalog(catalog))

  @GetMapping("/{id}")
  fun getCatalogById(@PathVariable(value = "id") catalogId: String): ResponseEntity<Catalog> =
    catalogService.getCatalogById(catalogId).map { catalog ->
      ResponseEntity.ok(catalog)
    }.orElse(ResponseEntity.notFound().build())

  @PutMapping("/{id}")
  fun updateCatalogById(
    @PathVariable(value = "id") catalogId: String,
    @Valid @RequestBody newCatalog: Catalog): ResponseEntity<Catalog> =
    catalogService.putCatalog(catalogId, newCatalog)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") catalogId: String): ResponseEntity<Void> =
    if (catalogService.deleteCatalog(catalogId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
