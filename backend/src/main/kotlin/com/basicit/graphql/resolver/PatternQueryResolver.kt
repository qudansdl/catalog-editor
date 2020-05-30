package com.basicit.graphql.resolver

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Category
import com.basicit.model.Pattern
import graphql.kickstart.tools.GraphQLQueryResolver
import com.basicit.service.PatternService
import org.springframework.stereotype.Component
import java.util.*

@Component
class PatternQueryResolver (private val patternService: PatternService) : GraphQLQueryResolver {
  fun patterns(input: DataTablesInput?): DataTablesOutput<Pattern> = patternService.getPatterns(input)

  fun pattern(patternId: UUID): Pattern = patternService.getPatternById(patternId).orElse(null)
}
