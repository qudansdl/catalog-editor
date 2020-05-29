package com.basicit.model

import java.util.*
import javax.persistence.*

/**
 * Template Block Entity
 *
 * @author poh
 */
@Entity
@Table(name = "template_block")
class TemplateBlock(
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
        @JoinColumn(name = "template_id")
        val template: Template? = null
) : AbstractEntity(id)
