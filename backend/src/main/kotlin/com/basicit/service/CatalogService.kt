package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.repository.CatalogRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CatalogService(
        private val catalogRepository: CatalogRepository
) {

  fun getCatalogs(input: DataTablesInput?): DataTablesOutput<Catalog>  =
          catalogRepository.findAll(input)

  fun addCatalog(catalog: Catalog): Catalog =
      catalogRepository.save(catalog)

  fun getCatalogById(catalogId: UUID): Optional<Catalog> =
          catalogRepository.findById(catalogId)

  fun putCatalog(
          catalogId: UUID,
          newCatalog: Catalog
  ): Optional<Catalog> =
      catalogRepository.findById(catalogId).map { currentCatalog ->
          currentCatalog.id
          currentCatalog.name = newCatalog.name
          currentCatalog.content = newCatalog.content
          currentCatalog.image = newCatalog.image
          currentCatalog.thumbnail = newCatalog.thumbnail
          currentCatalog.categories = newCatalog.categories

          catalogRepository.save(currentCatalog)
    }

  fun deleteCatalog(catalogId: UUID): Boolean =
          catalogRepository.findById(catalogId).map { catalog ->
              catalogRepository.delete(catalog)
      true
    }.orElse(false)
}
