package com.basicit.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import javax.persistence.*

/**
 * Catalog Entity
 *
 * @author poh
 */
@Entity
@Table(name = "catalog")
class Catalog(
        id: UUID? = null,
        val name: String? = null,
        val description: String? = null,

        // Relation many to many
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "catalog_categories",
                joinColumns = [JoinColumn(name = "catalog_id", nullable = false, updatable = false)],
                inverseJoinColumns = [JoinColumn(name = "category_id", nullable = false, updatable = false)]
        )
        @Fetch(FetchMode.SELECT)
        var categories: MutableSet<Category> = mutableSetOf(),

        @OneToMany(mappedBy = "catalog", fetch = FetchType.EAGER)
        @Fetch(FetchMode.SELECT)
        var blocks: MutableSet<CatalogBlock> = mutableSetOf()
) : AbstractEntity(id)
