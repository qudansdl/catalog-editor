package com.basicit.graphql.resolver

import com.basicit.model.Background
import com.basicit.service.BackgroundService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.apache.tika.Tika
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Part
import javax.xml.bind.DatatypeConverter




@Component
class BackgroundMutationResolver (private val backgroundService: BackgroundService) : GraphQLMutationResolver {
  fun createBackground(name: String?, content: String): Background {
    return backgroundService.addBackground(
      Background(
         name = name,
         content = content
      )
    )
   }

  fun updateBackground(backgroundId: UUID, name: String?, content: String): Background =
    backgroundService.putBackground(
      backgroundId,
      Background(
         id = backgroundId,
         name = name,
         content = content
      )
    ).orElse(null)

  fun deleteBackground(backgroundId: UUID): Boolean = backgroundService.deleteBackground(backgroundId)
}
