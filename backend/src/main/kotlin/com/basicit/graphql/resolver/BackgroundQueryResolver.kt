package com.basicit.graphql.resolver

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Category
import com.basicit.model.Background
import graphql.kickstart.tools.GraphQLQueryResolver
import com.basicit.service.BackgroundService
import org.springframework.stereotype.Component
import java.util.*

@Component
class BackgroundQueryResolver (private val backgroundService: BackgroundService) : GraphQLQueryResolver {
  fun backgrounds(input: DataTablesInput?): DataTablesOutput<Background> = backgroundService.getBackgrounds(input)

  fun background(backgroundId: UUID): Background = backgroundService.getBackgroundById(backgroundId).orElse(null)
}
