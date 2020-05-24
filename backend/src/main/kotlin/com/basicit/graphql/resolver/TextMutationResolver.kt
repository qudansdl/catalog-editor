package com.basicit.graphql.resolver

import com.basicit.model.Text
import graphql.kickstart.tools.GraphQLMutationResolver
import com.basicit.service.TextService
import org.springframework.stereotype.Component
import java.util.*

@Component
class TextMutationResolver (private val textService: TextService) : GraphQLMutationResolver {
  fun createText(content: String? = null): Text =
    textService.addText(
      Text(content = content)
    )

  fun updateText(textId: UUID, content: String? = null): Text =
    textService.putText(
      textId,
      Text(id = textId, content = content)
    ).orElse(null)

  fun deleteText(textId: UUID): Boolean = textService.deleteText(textId)
}
