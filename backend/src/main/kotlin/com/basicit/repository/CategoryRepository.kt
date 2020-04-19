package com.basicit.repository

import com.basicit.model.Category
import com.basicit.datatables.repository.DataTablesRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface CategoryRepository : DataTablesRepository<Category, String>
