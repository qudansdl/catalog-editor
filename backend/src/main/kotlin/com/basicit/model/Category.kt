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

    var name: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var parent: Category?,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    var children: Set<Category>?
) : AbstractEntity(id)
