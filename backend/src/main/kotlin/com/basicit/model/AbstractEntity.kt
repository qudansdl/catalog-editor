package com.basicit.model

import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.PostLoad
import javax.persistence.PostPersist

/**
 * Kudos to https://jivimberg.io/blog/2018/11/05/using-uuid-on-spring-data-jpa-entities/
 */
@MappedSuperclass
abstract class AbstractEntity(givenId: UUID? = null) {

    @Id
    @Column(name = "id", length = 36, unique = true, nullable = false)
    val id: UUID = givenId ?: UUID.randomUUID()

    @Transient
    var persisted: Boolean = givenId != null

    fun isNew(): Boolean = !persisted

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null -> false
            other !is AbstractEntity -> false
            else -> id == other.id
        }
    }

    @PostPersist
    @PostLoad
    private fun setPersisted() {
        persisted = true
    }
}
