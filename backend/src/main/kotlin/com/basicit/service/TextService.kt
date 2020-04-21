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

  fun getTextById(textId: String): Optional<Text> =
          textRepository.findById(textId)

  fun putText(
          textId: String,
          newText: Text
  ): Optional<Text> =
      textRepository.findById(textId).map { currentText ->
      val updatedText = Text(
              currentText.id,
              mutableSetOf(),
              newText.content)

          textRepository.save(updatedText)
    }

  fun deleteText(textId: String): Boolean =
          textRepository.findById(textId).map { text ->
              textRepository.delete(text)
      true
    }.orElse(false)
}
