package com.basicit.graphql.resolver

import com.basicit.model.Template
import com.basicit.model.Category
import com.basicit.service.TemplateService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class TemplateMutationResolver (private val templateService: TemplateService) : GraphQLMutationResolver {
  fun createTemplate(name: String? = null,
                    content: String? = null,
                     image: String? = null,
                     thumbnail: String? = null,
                    categories: MutableSet<Category> = mutableSetOf()
  ): Template =
    templateService.addTemplate(
      Template(
              name = name,
              content = content,
              image = image,
              thumbnail = thumbnail,
              categories = categories
      )
    )

  fun updateTemplate(templateId: UUID,
                    name: String? = null,
                    content: String? = null,
                     image: String? = null,
                     thumbnail: String? = null,
                    categories: MutableSet<Category> = mutableSetOf()): Template =
    templateService.putTemplate(
      templateId,
      Template(
          id = templateId,
          name = name,
          content = content,
          image = image,
          thumbnail = thumbnail,
          categories = categories
      )
    ).orElse(null)

  fun deleteTemplate(templateId: UUID): Boolean = templateService.deleteTemplate(templateId)
}
