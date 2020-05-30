package com.basicit.graphql.resolver

import com.basicit.model.Text
import graphql.kickstart.tools.GraphQLMutationResolver
import com.basicit.service.TextService
import org.springframework.stereotype.Component
import java.util.*

@Component
class TextMutationResolver (private val textService: TextService) : GraphQLMutationResolver {
  fun createText(name: String, content: String? = null): Text =
    textService.addText(
      Text(name = name, content = content)
    )

  fun updateText(textId: UUID, name: String, content: String? = null): Text =
    textService.putText(
      textId,
      Text(id = textId, name = name, content = content)
    ).orElse(null)

  fun deleteText(textId: UUID): Boolean = textService.deleteText(textId)
}
