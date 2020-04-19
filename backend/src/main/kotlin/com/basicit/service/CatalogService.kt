package com.basicit.service

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.repository.CatalogRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CatalogService(private val catalogRepository: CatalogRepository) {

  fun getCatalogs(input: DataTablesInput?): DataTablesOutput<Catalog>  =
          catalogRepository.findAll(input)

  fun addCatalog(catalog: Catalog): Catalog = catalogRepository.save(catalog)

  fun getCatalogById(catalogId: String): Optional<Catalog> =
          catalogRepository.findById(catalogId)

  fun putCatalog(
          catalogId: String,
          newCatalog: Catalog
  ): Optional<Catalog> =
      catalogRepository.findById(catalogId).map { currentCatalog ->
      val updatedCatalog = Catalog(
              currentCatalog.id,
              newCatalog.name,
              newCatalog.description,
              newCatalog.categories,
              newCatalog.blocks)

          catalogRepository.save(updatedCatalog)
    }

  fun deleteCatalog(catalogId: String): Boolean =
          catalogRepository.findById(catalogId).map { catalog ->
              catalogRepository.delete(catalog)
      true
    }.orElse(false)
}
