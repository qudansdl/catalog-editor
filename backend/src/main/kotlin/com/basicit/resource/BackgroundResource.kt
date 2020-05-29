package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.model.Background
import com.basicit.service.BackgroundService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/api/backgrounds")
class BackgroundResource(private val backgroundService: BackgroundService) {

  @GetMapping
  fun getBackgrounds(input: DataTablesInput): DataTablesOutput<Background> = backgroundService.getBackgrounds(input)

  @PostMapping
  fun addBackground(@Valid @RequestBody background: Background): ResponseEntity<Background> =
    ResponseEntity.ok(backgroundService.addBackground(background))

  @GetMapping("/{id}")
  fun getBackgroundById(@PathVariable(value = "id") backgroundId: UUID): ResponseEntity<Background> =
    backgroundService.getBackgroundById(backgroundId).map { background ->
      ResponseEntity.ok(background)
    }.orElse(ResponseEntity.notFound().build())

  @PutMapping("/{id}")
  fun updateBackgroundById(
          @PathVariable(value = "id") backgroundId: UUID,
          @Valid @RequestBody newBackground: Background): ResponseEntity<Background> =
    backgroundService.putBackground(backgroundId, newBackground)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") backgroundId: UUID): ResponseEntity<Void> =
    if (backgroundService.deleteBackground(backgroundId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
