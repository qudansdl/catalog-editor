package com.basicit.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*


/**
 * Kudos to https://jivimberg.io/blog/2018/11/05/using-uuid-on-spring-data-jpa-entities/
 */
@MappedSuperclass
abstract class AbstractEntity(givenId: UUID? = null) {

    @Id
    @Column(name = "id", length = 36, unique = true, nullable = false)
    val id: UUID = givenId ?: UUID.randomUUID()

    @CreatedDate
    private val created: Date? = null

    @LastModifiedDate
    private val updated: Date? = null


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
