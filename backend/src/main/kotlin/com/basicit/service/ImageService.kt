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

  fun getImageById(imageId: String): Optional<Image> =
          imageRepository.findById(imageId)

  fun putImage(
          imageId: String,
          newImage: Image
  ): Optional<Image> =
      imageRepository.findById(imageId).map { currentImage ->
      val updatedImage = Image(
              currentImage.id,
              mutableListOf(),
              newImage.content)

          imageRepository.save(updatedImage)
    }

  fun deleteImage(imageId: String): Boolean =
          imageRepository.findById(imageId).map { image ->
              imageRepository.delete(image)
      true
    }.orElse(false)
}
