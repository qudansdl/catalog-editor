package com.basicit.domain

import com.basicit.model.AbstractEntity
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.EntityPathBase
import org.apache.commons.lang3.reflect.FieldUtils
import java.lang.reflect.Modifier
import java.util.*

class QClass(
        private val entityClass: Class<AbstractEntity>,
        private val qClass: Class<EntityPathBase<AbstractEntity>>
) {
    val instance: EntityPathBase<AbstractEntity>
    val id: ComparablePath<UUID>

    init {
        instance = createQClassInstance()
        id = getIdPath()

    }

    private fun createQClassInstance(): EntityPathBase<AbstractEntity> {
        val fieldList = FieldUtils.getAllFieldsList(this.qClass)
        var qClassInstance: EntityPathBase<AbstractEntity>? = null
        for(field in fieldList)
        {
            if(Modifier.isStatic(field.modifiers) &&   field.type == this.qClass)
            {
                qClassInstance  = field.get(null) as EntityPathBase<AbstractEntity>
                break
            }
        }

        return qClassInstance!!
    }

    private fun getIdPath(): ComparablePath<UUID> {
        return getPath("id") as ComparablePath<UUID>
    }

    fun getPath(path: String): Path<Comparable<*>> {
        val idField = FieldUtils.getField(qClass, path)
        return idField.get(this.instance) as Path<Comparable<*>>
    }
}
