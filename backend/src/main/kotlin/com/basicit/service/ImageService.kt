package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Image
import com.basicit.repository.ImageRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ImageService(private val imageRepository: ImageRepository) {

  fun getImages(input: DataTablesInput?): DataTablesOutput<Image> = imageRepository.findAll(input)

  fun addImage(image: Image): Image = imageRepository.save(image)

  fun getImageById(imageId: UUID): Optional<Image> = imageRepository.findById(imageId)

  fun putImage(
          imageId: UUID,
          newImage: Image
  ): Optional<Image> =
      imageRepository.findById(imageId).map { currentImage ->
          currentImage.name = newImage.name
          currentImage.content = newImage.content
          imageRepository.save(currentImage)
    }

  fun deleteImage(imageId: UUID): Boolean =
          imageRepository.findById(imageId).map { image -> imageRepository.delete(image)
      true
    }.orElse(false)
}
