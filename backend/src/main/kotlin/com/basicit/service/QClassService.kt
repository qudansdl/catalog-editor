package com.basicit.service

import com.basicit.domain.QClass
import com.basicit.model.AbstractEntity
import com.querydsl.core.types.dsl.EntityPathBase
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.stereotype.Service
import java.lang.reflect.ParameterizedType

@Service
class QClassService<T> {
    private val qClassMap = mutableMapOf<Class<T>, Class<EntityPathBase<T>>>()

    init {
        initQClasses("com")
    }

    private fun initQClasses(scanPackage: String?) {
        val provider = createComponentScanner()
        for (beanDef in provider.findCandidateComponents(scanPackage!!)) {
            val cl = Class.forName(beanDef.beanClassName)
            if (cl.genericSuperclass is ParameterizedType) {
                val actualTypeArgument = (cl.genericSuperclass as ParameterizedType).actualTypeArguments[0]
                if (actualTypeArgument is Class<*>) {
                    val qClass = cl as Class<EntityPathBase<T>>
                    val persistentClass = actualTypeArgument as Class<T>
                    qClassMap.put(persistentClass, qClass)
                }
            }
        }
    }

    private fun createComponentScanner(): ClassPathScanningCandidateComponentProvider {
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AssignableTypeFilter(EntityPathBase::class.java))
        return provider
    }

    fun  getQClass(clazz: Class<AbstractEntity>): QClass {
        return QClass(clazz, qClassMap.get(clazz) as Class<EntityPathBase<Any>>)
    }

}
