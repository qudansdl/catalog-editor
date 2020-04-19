package com.basicit.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Tag Entity
 *
 * @author poh
 */
@Entity
@Table(name = "category")
class Category(id: UUID? = null, val name: String? = null) : AbstractEntity(id)
