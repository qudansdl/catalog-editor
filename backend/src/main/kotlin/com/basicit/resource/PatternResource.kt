package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.model.Pattern
import com.basicit.service.PatternService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/api/patterns")
class PatternResource(private val patternService: PatternService) {

  @GetMapping
  fun getPatterns(input: DataTablesInput): DataTablesOutput<Pattern> = patternService.getPatterns(input)

  @PostMapping
  fun addPattern(@Valid @RequestBody pattern: Pattern): ResponseEntity<Pattern> =
    ResponseEntity.ok(patternService.addPattern(pattern))

  @GetMapping("/{id}")
  fun getPatternById(@PathVariable(value = "id") patternId: UUID): ResponseEntity<Pattern> =
    patternService.getPatternById(patternId).map { pattern ->
      ResponseEntity.ok(pattern)
    }.orElse(ResponseEntity.notFound().build())

  @PutMapping("/{id}")
  fun updatePatternById(
          @PathVariable(value = "id") patternId: UUID,
          @Valid @RequestBody newPattern: Pattern): ResponseEntity<Pattern> =
    patternService.putPattern(patternId, newPattern)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") patternId: UUID): ResponseEntity<Void> =
    if (patternService.deletePattern(patternId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
