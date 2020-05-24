package com.basicit.graphql.resolver

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Category
import com.basicit.model.Image
import graphql.kickstart.tools.GraphQLQueryResolver
import com.basicit.service.ImageService
import org.springframework.stereotype.Component
import java.util.*

@Component
class ImageQueryResolver (private val imageService: ImageService) : GraphQLQueryResolver {
  fun images(input: DataTablesInput?): DataTablesOutput<Image> = imageService.getImages(input)

  fun image(imageId: UUID): Image = imageService.getImageById(imageId).orElse(null)
}
