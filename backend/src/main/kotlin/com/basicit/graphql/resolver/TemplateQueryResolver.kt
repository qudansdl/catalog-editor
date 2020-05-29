package com.basicit.graphql.resolver

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Template
import com.basicit.model.Category
import com.basicit.service.TemplateService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class TemplateQueryResolver (
        private val templateService: TemplateService
) : GraphQLQueryResolver {
  fun templates(input: DataTablesInput?): DataTablesOutput<Template> = templateService.getTemplates(input)
  fun template(templateId: UUID): Template = templateService.getTemplateById(templateId).orElse(null)
}
