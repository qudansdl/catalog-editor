package com.basicit.repository

import com.basicit.model.Catalog
import com.basicit.datatables.repository.DataTablesRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface CatalogRepository : DataTablesRepository<Catalog, UUID>
