package com.basicit.graphql.resolver

import com.basicit.model.Category
import com.basicit.model.Pattern
import com.basicit.service.PatternService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class PatternMutationResolver (private val patternService: PatternService) : GraphQLMutationResolver {
  fun createPattern(
          name: String?,
          content: String,
          categories: MutableSet<Category> = mutableSetOf()
  ): Pattern {
    return patternService.addPattern(
      Pattern(
         name = name,
         content = content,
         categories = categories
      )
    )
   }

  fun updatePattern(
          patternId: UUID,
          name: String?,
          content: String,
          categories: MutableSet<Category> = mutableSetOf()
  ): Pattern =
    patternService.putPattern(
      patternId,
      Pattern(
         id = patternId,
         name = name,
         content = content,
         categories = categories
      )
    ).orElse(null)

  fun deletePattern(patternId: UUID): Boolean = patternService.deletePattern(patternId)
}
