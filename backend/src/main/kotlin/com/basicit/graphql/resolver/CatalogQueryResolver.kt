package com.basicit.graphql.resolver

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.Catalog
import com.basicit.model.Category
import com.basicit.service.CatalogService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class CatalogQueryResolver (
        private val catalogService: CatalogService
) : GraphQLQueryResolver {
  fun catalogs(input: DataTablesInput?): DataTablesOutput<Catalog> = catalogService.getCatalogs(input)
  fun catalog(catalogId: UUID): Catalog = catalogService.getCatalogById(catalogId).orElse(null)
}
