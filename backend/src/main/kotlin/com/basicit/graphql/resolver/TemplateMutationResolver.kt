package com.basicit.graphql.resolver

import com.basicit.model.TemplateBlock
import com.basicit.model.Template
import com.basicit.model.Category
import com.basicit.service.TemplateService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class TemplateMutationResolver (private val templateService: TemplateService) : GraphQLMutationResolver {
  fun createTemplate(name: String? = null,
                    description: String? = null,
                    categories: MutableSet<Category> = mutableSetOf(),
                    blocks: MutableSet<TemplateBlock> = mutableSetOf()
  ): Template =
    templateService.addTemplate(
      Template(
              name = name,
              description = description,
              categories = categories,
              blocks = blocks
      )
    )

  fun updateTemplate(templateId: UUID,
                    name: String? = null,
                    description: String? = null,
                    categories: MutableSet<Category> = mutableSetOf(),
                    blocks: MutableSet<TemplateBlock> = mutableSetOf()): Template =
    templateService.putTemplate(
      templateId,
      Template(
          id = templateId,
          name = name,
          description = description,
          categories = categories,
          blocks = blocks
      )
    ).orElse(null)

  fun deleteTemplate(templateId: UUID): Boolean = templateService.deleteTemplate(templateId)
}
