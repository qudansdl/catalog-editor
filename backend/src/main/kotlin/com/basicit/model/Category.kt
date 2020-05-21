package com.basicit.model

import java.util.*
import javax.persistence.*

/**
 * Category Entity
 *
 * @author poh
 */
@Entity
@Table(name = "category")
class Category(
    id: UUID? = null,

    val name: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val parent: Category?,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    val children: Set<Category>?
) : AbstractEntity(id)
