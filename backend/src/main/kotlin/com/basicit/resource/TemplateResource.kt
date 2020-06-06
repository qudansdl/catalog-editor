package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.html2pdf.common.DirectoryHelper
import com.basicit.model.Template
import com.basicit.service.TemplateService
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
@RequestMapping("/v1/api/templates")
class TemplateResource(private val templateService: TemplateService) {

  @GetMapping
  fun getTemplates(input: DataTablesInput): DataTablesOutput<Template> = templateService.getTemplates(input)

  @PostMapping
  fun addTemplate(@Valid @RequestBody template: Template): ResponseEntity<Template> =
    ResponseEntity.ok(templateService.addTemplate(template))

  @GetMapping("/{id}")
  fun getTemplateById(@PathVariable(value = "id") templateId: UUID): ResponseEntity<Template> =
    templateService.getTemplateById(templateId).map { template ->
      ResponseEntity.ok(template)
    }.orElse(ResponseEntity.notFound().build())


  @GetMapping("/{id}/download")
  fun downloadById(@PathVariable(value = "id") templateId: UUID): ResponseEntity<Resource> {
    val documentsDirectory = "webapp/documents"

    val headers = HttpHeaders()
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate")
    headers.add("Pragma", "no-cache")
    headers.add("Expires", "0")
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$templateId.pdf");

    val pdf = DirectoryHelper.joinFromRoot(documentsDirectory) + String.format("/%s.pdf", templateId)
    val resource = FileSystemResource(pdf)
    return ResponseEntity.ok()
            .headers(headers)
            .contentLength(resource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
  }

  @PutMapping("/{id}")
  fun updateTemplateById(
    @PathVariable(value = "id") templateId: UUID,
    @Valid @RequestBody newTemplate: Template): ResponseEntity<Template> =
    templateService.putTemplate(templateId, newTemplate)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") templateId: UUID): ResponseEntity<Void> =
    if (templateService.deleteTemplate(templateId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
