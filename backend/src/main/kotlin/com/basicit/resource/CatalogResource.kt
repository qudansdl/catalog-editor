package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.html2pdf.common.DirectoryHelper
import com.basicit.model.Catalog
import com.basicit.service.CatalogService
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
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
  fun getCatalogById(@PathVariable(value = "id") catalogId: UUID): ResponseEntity<Catalog> =
    catalogService.getCatalogById(catalogId).map { catalog ->
      ResponseEntity.ok(catalog)
    }.orElse(ResponseEntity.notFound().build())

  @GetMapping("/{id}/download")
  fun downloadById(@PathVariable(value = "id") catalogId: UUID): ResponseEntity<Resource> {
    val documentsDirectory = "webapp/documents"

    val headers = HttpHeaders()
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate")
    headers.add("Pragma", "no-cache")
    headers.add("Expires", "0")
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$catalogId.pdf");

    val pdf = DirectoryHelper.joinFromRoot(documentsDirectory) + String.format("/%s.pdf", catalogId)
    val resource = FileSystemResource(pdf)
    return ResponseEntity.ok()
            .headers(headers)
            .contentLength(resource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
  }

  @PutMapping("/{id}")
  fun updateCatalogById(
    @PathVariable(value = "id") catalogId: UUID,
    @Valid @RequestBody newCatalog: Catalog): ResponseEntity<Catalog> =
    catalogService.putCatalog(catalogId, newCatalog)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") catalogId: UUID): ResponseEntity<Void> =
    if (catalogService.deleteCatalog(catalogId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
