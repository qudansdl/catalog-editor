package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Background
import com.basicit.repository.BackgroundRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BackgroundService(private val backgroundRepository: BackgroundRepository) {

  fun getBackgrounds(input: DataTablesInput?): DataTablesOutput<Background> = backgroundRepository.findAll(input)

  fun addBackground(background: Background): Background = backgroundRepository.save(background)

  fun getBackgroundById(backgroundId: UUID): Optional<Background> = backgroundRepository.findById(backgroundId)

  fun putBackground(
          backgroundId: UUID,
          newBackground: Background
  ): Optional<Background> =
      backgroundRepository.findById(backgroundId).map { currentBackground ->
          currentBackground.name = newBackground.name
          currentBackground.content = newBackground.content
          currentBackground.categories = newBackground.categories
          backgroundRepository.save(currentBackground)
    }

  fun deleteBackground(backgroundId: UUID): Boolean =
          backgroundRepository.findById(backgroundId).map { background -> backgroundRepository.delete(background)
      true
    }.orElse(false)
}
