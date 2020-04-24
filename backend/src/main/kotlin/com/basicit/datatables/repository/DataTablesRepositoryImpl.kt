package com.basicit.datatables.repository

import com.basicit.config.GraphQLConfig
import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.util.SpringUtils
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine
import com.querydsl.jpa.impl.JPAQueryFactory
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionService
import org.springframework.core.io.ClassPathResource
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.File
import java.io.Serializable
import java.nio.file.Files
import javax.persistence.EntityManager
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager


/*
* T : Entity,
 */
class DataTablesRepositoryImpl<T, ID : Serializable, P: Comparable<P>>(
        entityInformation: JpaEntityInformation<T, ID>,
        val entityManager: EntityManager
) :
        SimpleJpaRepository<T, ID>(entityInformation, entityManager),
        DataTablesRepository<T, ID>
{
    private val logger = LoggerFactory.getLogger(GraphQLConfig::class.java.name)


    override fun findAll(input: DataTablesInput?): DataTablesOutput<T> {


        val engine: ScriptEngine = GraalJSScriptEngine.create(null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.ALL)
                        .allowHostClassLookup({ s -> true })
                        .option("js.ecmascript-version", "2020"))

        val conversionServiceList = SpringUtils.getBeans(ConversionService::class.java)

        val output = DataTablesOutput<T>()

        val queryFactory = JPAQueryFactory(entityManager)



        val context = QueryContext<T>(
            input,
            output,
            queryFactory,
            domainClass,
            conversionServiceList
        )
        engine.put("context", context)


        // val js = File("C:\\projects\\beadnet\\sources\\catalog-editor\\backend\\src\\main\\resources\\querydsl.js").readText(Charsets.UTF_8)
        val resource = ClassPathResource("querydsl.js").getFile();
        val js = Files.readAllBytes(resource.toPath()).toString(Charsets.UTF_8)

        engine.eval(js)

        return output
    }
}
