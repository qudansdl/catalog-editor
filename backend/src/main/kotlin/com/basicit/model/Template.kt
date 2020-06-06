package com.basicit.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import javax.persistence.*

/**
 * Template Entity
 *
 * @author poh
 */
@Entity
@Table(name = "template")
class Template(
        id: UUID? = null,
        var name: String? = null,
        var content: String? = null,
        var image: String? = null,
        var thumbnail: String? = null,

        // Relation many to many
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "template_categories",
                joinColumns = [JoinColumn(name = "template_id", nullable = false, updatable = false)],
                inverseJoinColumns = [JoinColumn(name = "category_id", nullable = false, updatable = false)]
        )
        @Fetch(FetchMode.SELECT)
        var categories: MutableSet<Category> = mutableSetOf()
) : AbstractEntity(id)
