package com.basicit.graphql.resolver

import com.basicit.model.Image
import com.basicit.service.ImageService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.apache.tika.Tika
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Part
import javax.xml.bind.DatatypeConverter




@Component
class ImageMutationResolver (private val imageService: ImageService) : GraphQLMutationResolver {
  fun createImage(name: String?, content: String): Image {
    return imageService.addImage(
      Image(
         name = name,
         content = content
      )
    )
   }

  fun updateImage(imageId: UUID, name: String?, content: String): Image =
    imageService.putImage(
      imageId,
      Image(
         id = imageId,
         name = name,
         content = content
      )
    ).orElse(null)

  fun deleteImage(imageId: UUID): Boolean = imageService.deleteImage(imageId)
}
