package com.basicit.util

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpringUtils : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        if (Companion.applicationContext == null) {
            Companion.applicationContext = applicationContext
        }
    }

    companion object {
        private var applicationContext: ApplicationContext? = null

        fun getBean(name: String?): Any {
            return applicationContext!!.getBean(name!!)
        }

        fun <T> getBean(clazz: Class<T>): T {
            return applicationContext!!.getBean(clazz)
        }

        fun <T> getBeans(clazz: Class<T>): List<T> {
            val names = applicationContext!!.getBeanNamesForType(clazz)
            val beans =  mutableListOf<T>()
            for(name in names)
            {
                beans.add(applicationContext!!.getBean(name) as T)
            }
            return beans
        }

        fun <T> getBean(name: String?, clazz: Class<T>): T {
            return applicationContext!!.getBean(name!!, clazz)
        }
    }
}
