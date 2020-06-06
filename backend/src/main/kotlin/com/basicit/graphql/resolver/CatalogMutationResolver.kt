package com.basicit.graphql.resolver

import com.basicit.model.Catalog
import com.basicit.model.Category
import com.basicit.service.CatalogService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class CatalogMutationResolver (private val catalogService: CatalogService) : GraphQLMutationResolver {
  fun createCatalog(name: String? = null,
                    content: String? = null,
                    image: String? = null,
                    thumbnail: String? = null,
                    categories: MutableSet<Category> = mutableSetOf()
  ): Catalog =
    catalogService.addCatalog(
      Catalog(
              name = name,
              content = content,
              image = image,
              thumbnail = thumbnail,
              categories = categories
      )
    )

  fun updateCatalog(catalogId: UUID,
                    name: String? = null,
                    content: String? = null,
                    image: String? = null,
                    thumbnail: String? = null,
                    categories: MutableSet<Category> = mutableSetOf()): Catalog =
    catalogService.putCatalog(
      catalogId,
      Catalog(
          id = catalogId,
          name = name,
          content = content,
          image = image,
          thumbnail = thumbnail,
          categories = categories
      )
    ).orElse(null)

  fun deleteCatalog(catalogId: UUID): Boolean = catalogService.deleteCatalog(catalogId)
}
