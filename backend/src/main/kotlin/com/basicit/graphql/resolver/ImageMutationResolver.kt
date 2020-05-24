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
  fun createBase64Image(part: Part) : String
  {
    val contentType  = Tika().detect(part.submittedFileName)

    part.inputStream.use {
      val base64str = DatatypeConverter.printBase64Binary(it.readBytes())
      val sb = StringBuilder()
      sb.append("data:")
      sb.append(contentType)
      sb.append(";base64,")
      sb.append(base64str)

      return sb.toString()
    }
  }

  fun createImage(part: Part): Image {
    return imageService.addImage(
      Image(content = createBase64Image(part))
    )
   }

  fun updateImage(imageId: UUID, part: Part): Image =
    imageService.putImage(
      imageId,
      Image(id = imageId, content = createBase64Image(part))
    ).orElse(null)

  fun deleteImage(imageId: UUID): Boolean = imageService.deleteImage(imageId)
}
