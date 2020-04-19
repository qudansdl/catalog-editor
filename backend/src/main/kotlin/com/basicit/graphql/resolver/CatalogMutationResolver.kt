package com.basicit.graphql.resolver

import com.basicit.model.CatalogBlock
import com.basicit.model.Catalog
import com.basicit.model.Category
import com.basicit.service.CatalogService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class CatalogMutationResolver (private val catalogService: CatalogService) : GraphQLMutationResolver {
  fun createCatalog(name: String? = null,
                    description: String? = null,
                    categories: List<Category> = ArrayList(),
                    blocks: List<CatalogBlock> = ArrayList()
  ): Catalog =
    catalogService.addCatalog(
      Catalog(
              name = name,
              description = description,
              categories = categories,
              blocks = blocks
      )
    )

  fun updateCatalog(catalogId: String,
                    name: String? = null,
                    description: String? = null,
                    categories: List<Category> = ArrayList(),
                    blocks: List<CatalogBlock> = ArrayList()): Catalog =
    catalogService.putCatalog(
      catalogId,
      Catalog(
          id = UUID.fromString(catalogId),
          name = name,
          description = description,
          categories = categories,
          blocks = blocks
      )
    ).orElse(null)

  fun deleteCatalog(catalogId: String): Boolean = catalogService.deleteCatalog(catalogId)
}
