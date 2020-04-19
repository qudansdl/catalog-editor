package com.basicit.repository

import com.basicit.model.Image
import com.basicit.datatables.repository.DataTablesRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface ImageRepository : DataTablesRepository<Image, String>
