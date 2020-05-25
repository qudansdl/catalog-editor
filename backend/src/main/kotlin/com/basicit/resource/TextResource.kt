package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Text
import com.basicit.service.TextService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/api/texts")
class TextResource(private val textService: TextService) {

  @GetMapping
  fun getTexts(input: DataTablesInput): DataTablesOutput<Text> = textService.getTexts(input)

  @PostMapping
  fun addText(@Valid @RequestBody text: Text): ResponseEntity<Text> =
    ResponseEntity.ok(textService.addText(text))

  @GetMapping("/{id}")
  fun getTextById(@PathVariable(value = "id") textId: UUID): ResponseEntity<Text> =
    textService.getTextById(textId).map { text ->
      ResponseEntity.ok(text)
    }.orElse(ResponseEntity.notFound().build())

  @PutMapping("/{id}")
  fun updateTextById(
          @PathVariable(value = "id") textId: UUID,
          @Valid @RequestBody newText: Text): ResponseEntity<Text> =
    textService.putText(textId, newText)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") textId: UUID): ResponseEntity<Void> =
    if (textService.deleteText(textId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
