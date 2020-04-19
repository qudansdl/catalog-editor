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

  fun updateText(textId: String, content: String? = null): Text =
    textService.putText(
      textId,
      Text(id = UUID.fromString(textId), content = content)
    ).orElse(null)

  fun deleteText(textId: String): Boolean = textService.deleteText(textId)
}
