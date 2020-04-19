package com.basicit.model

import java.util.*
import javax.persistence.*

/**
 * Block Entity
 *
 * @author poh
 */
@Entity
@Table(name = "catalog_block")
class CatalogBlock(
        id: UUID? = null,
        val x:Int,
        val y:Int,
        val w:Int,
        val h:Int,

        @Column(length = 30)
        @Enumerated(EnumType.STRING)
        val type: TypeEnum? = null,

        val content: String? = null,

        @ManyToOne
        @JoinColumn(name = "catalog_id")
        val catalog: Catalog? = null
) : AbstractEntity(id)
