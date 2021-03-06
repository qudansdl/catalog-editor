package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Category
import com.basicit.model.Image
import com.basicit.model.Text
import com.basicit.repository.TextRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TextService(private val textRepository: TextRepository) {

  fun getTexts(input: DataTablesInput?): DataTablesOutput<Text> = textRepository.findAll(input)

  fun addText(text: Text): Text = textRepository.save(text)

  fun getTextById(textId: UUID): Optional<Text> =
          textRepository.findById(textId)

  fun putText(
          textId: UUID,
          newText: Text
  ): Optional<Text> =
      textRepository.findById(textId).map { currentText ->
          currentText.name = newText.name
          currentText.content = newText.content
          currentText.categories = newText.categories

          textRepository.save(currentText)
    }

  fun deleteText(textId: UUID): Boolean =
          textRepository.findById(textId).map { text ->
              textRepository.delete(text)
      true
    }.orElse(false)
}
