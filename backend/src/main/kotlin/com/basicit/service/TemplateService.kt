package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Template
import com.basicit.repository.TemplateRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TemplateService(private val templateRepository: TemplateRepository) {

  fun getTemplates(input: DataTablesInput?): DataTablesOutput<Template>  =
          templateRepository.findAll(input)

  fun addTemplate(template: Template): Template = templateRepository.save(template)

  fun getTemplateById(templateId: UUID): Optional<Template> =
          templateRepository.findById(templateId)

  fun putTemplate(
          templateId: UUID,
          newTemplate: Template
  ): Optional<Template> =
      templateRepository.findById(templateId).map { currentTemplate ->
      val updatedTemplate = Template(
              currentTemplate.id,
              newTemplate.name,
              newTemplate.content,
              newTemplate.categories)

          templateRepository.save(updatedTemplate)
    }

  fun deleteTemplate(templateId: UUID): Boolean =
          templateRepository.findById(templateId).map { template ->
              templateRepository.delete(template)
      true
    }.orElse(false)
}
