package com.basicit.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import javax.persistence.*

/**
 * Background Entity
 *
 * @author poh
 */
@Entity
@Table(name = "pattern")
class Pattern(
        id: UUID? = null,

        var name: String? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "pattern_categories",
                joinColumns = [JoinColumn(name = "pattern_id", nullable = false, updatable = false)],
                inverseJoinColumns = [JoinColumn(name = "category_id", nullable = false, updatable = false)]
        )
        @Fetch(FetchMode.SELECT)
        var categories: MutableSet<Category> = mutableSetOf(),

        var content: String? = null
) : AbstractEntity(id)
