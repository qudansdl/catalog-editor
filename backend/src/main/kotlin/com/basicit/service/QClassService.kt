package com.basicit.service

import com.basicit.domain.QClass
import com.basicit.model.AbstractEntity
import com.querydsl.core.types.dsl.EntityPathBase

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.stereotype.Service
import java.lang.reflect.ParameterizedType

@Service
class QClassService {
    private val qClassMap = mutableMapOf<Class<*>, QClass>()

    companion object {
        @JvmStatic lateinit var instance: QClassService
    }

    init {
        initQClasses("com")
        instance = this
    }



    private fun initQClasses(scanPackage: String?) {
        val provider = createComponentScanner()
        for (beanDef in provider.findCandidateComponents(scanPackage!!)) {
            val cl = Class.forName(beanDef.beanClassName)
            if (cl.genericSuperclass is ParameterizedType) {
                val actualTypeArgument = (cl.genericSuperclass as ParameterizedType).actualTypeArguments[0]
                if (actualTypeArgument is Class<*>) {
                    val persistentClass = actualTypeArgument as Class<AbstractEntity>
                    val qClass = cl as Class<EntityPathBase<AbstractEntity>>

                    qClassMap[persistentClass] = QClass(persistentClass, qClass)
                }
            }
        }
    }

    private fun createComponentScanner(): ClassPathScanningCandidateComponentProvider {
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AssignableTypeFilter(EntityPathBase::class.java))
        return provider
    }

    fun getQClass(clazz: Class<*>): QClass {
        return qClassMap[clazz]!!
    }

}
