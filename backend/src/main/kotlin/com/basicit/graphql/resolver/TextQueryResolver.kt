package com.basicit.graphql.resolver

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Image
import com.basicit.model.Text
import graphql.kickstart.tools.GraphQLQueryResolver
import com.basicit.service.TextService
import org.springframework.stereotype.Component

@Component
class TextQueryResolver (private val textService: TextService) : GraphQLQueryResolver {
  fun texts(input: DataTablesInput?): DataTablesOutput<Text> = textService.getTexts(input)

  fun text(textId: String): Text = textService.getTextById(textId).orElse(null)
}
