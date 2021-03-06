package com.basicit.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.util.*
import javax.persistence.*

/**
 * Text Entity
 *
 * @author poh
 */
@Entity
@Table(name = "text")
class Text(
        id: UUID? = null,

        var name: String? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "text_categories",
                joinColumns = [JoinColumn(name = "text_id")],
                inverseJoinColumns = [JoinColumn(name = "category_id")]

        )
        @Fetch(FetchMode.SELECT)
        var categories: MutableSet<Category> = mutableSetOf(),

        var content: String? = null
) : AbstractEntity(id)
