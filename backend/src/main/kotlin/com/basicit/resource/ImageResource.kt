package com.basicit.resource

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.model.Image
import com.basicit.service.ImageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/api/images")
class ImageResource(private val imageService: ImageService) {

  @GetMapping
  fun getImages(input: DataTablesInput): DataTablesOutput<Image> = imageService.getImages(input)

  @PostMapping
  fun addImage(@Valid @RequestBody image: Image): ResponseEntity<Image> =
    ResponseEntity.ok(imageService.addImage(image))

  @GetMapping("/{id}")
  fun getImageById(@PathVariable(value = "id") imageId: UUID): ResponseEntity<Image> =
    imageService.getImageById(imageId).map { image ->
      ResponseEntity.ok(image)
    }.orElse(ResponseEntity.notFound().build())

  @PutMapping("/{id}")
  fun updateImageById(
          @PathVariable(value = "id") imageId: UUID,
          @Valid @RequestBody newImage: Image): ResponseEntity<Image> =
    imageService.putImage(imageId, newImage)
      .map { task -> ResponseEntity.ok().body(task) }
      .orElse(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable(value = "id") imageId: UUID): ResponseEntity<Void> =
    if (imageService.deleteImage(imageId))
      ResponseEntity<Void>(HttpStatus.ACCEPTED)
    else
      ResponseEntity.notFound().build()
}
