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
                    categories: MutableSet<Category> = mutableSetOf(),
                    blocks: MutableSet<CatalogBlock> = mutableSetOf()
  ): Catalog =
    catalogService.addCatalog(
      Catalog(
              name = name,
              description = description,
              categories = categories,
              blocks = blocks
      )
    )

  fun updateCatalog(catalogId: UUID,
                    name: String? = null,
                    description: String? = null,
                    categories: MutableSet<Category> = mutableSetOf(),
                    blocks: MutableSet<CatalogBlock> = mutableSetOf()): Catalog =
    catalogService.putCatalog(
      catalogId,
      Catalog(
          id = catalogId,
          name = name,
          description = description,
          categories = categories,
          blocks = blocks
      )
    ).orElse(null)

  fun deleteCatalog(catalogId: UUID): Boolean = catalogService.deleteCatalog(catalogId)
}
