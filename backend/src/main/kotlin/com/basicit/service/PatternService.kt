package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Pattern
import com.basicit.repository.PatternRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PatternService(private val patternRepository: PatternRepository) {

  fun getPatterns(input: DataTablesInput?): DataTablesOutput<Pattern> = patternRepository.findAll(input)

  fun addPattern(pattern: Pattern): Pattern = patternRepository.save(pattern)

  fun getPatternById(patternId: UUID): Optional<Pattern> = patternRepository.findById(patternId)

  fun putPattern(
          patternId: UUID,
          newPattern: Pattern
  ): Optional<Pattern> =
      patternRepository.findById(patternId).map { currentPattern ->
          currentPattern.name = newPattern.name
          currentPattern.content = newPattern.content
          currentPattern.categories = newPattern.categories
          patternRepository.save(currentPattern)
    }

  fun deletePattern(patternId: UUID): Boolean =
          patternRepository.findById(patternId).map { pattern -> patternRepository.delete(pattern)
      true
    }.orElse(false)
}
