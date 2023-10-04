package ru.vood.processor.datamodel.abstraction

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntityColumn
import ru.vood.processor.datamodel.abstraction.model.MetaForeignKey
import ru.vood.processor.datamodel.abstraction.model.ModelClassName
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("ru.vood.dmgen.annotation.FlowEntity")
class MetaDataKtAnnotationProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val elementsAnnotatedWithFlowEntity = roundEnv.getElementsAnnotatedWith(FlowEntity::class.java)

        val allMeta: Map<Element, MetaEntity> = collectMetaEntity(elementsAnnotatedWithFlowEntity)


        val entities: Map<ModelClassName, MetaEntity> =
            allMeta.map { ModelClassName(it.value.kotlinMetaClass.toString()) to it.value }.toMap()

        val elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(ForeignKey::class.java)


        val fks = entities.map { it.value }
            .flatMap { qw ->
                qw.foreignKeysAnnotations.map { fk -> fk to ModelClassName(qw.kotlinMetaClass.toString()) }
            }

        collectMetaForeignKey(fks, entities)


        roundEnv.getElementsAnnotatedWith(FlowEntity::class.java).forEach { classElement ->
//            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "${classElement.simpleName} is processed.")
            kotlin.runCatching {
                classElement.asType().asTypeName()
                val metaEntity = MetaEntity(classElement)
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    "${classElement.simpleName} ${classElement.isKotlinElement()} is processed."
                )


                metaEntity.uniqueKeysFields
                    .forEach { colMap ->
                        processingEnv.messager.printMessage(
                            Diagnostic.Kind.WARNING,
                            "- UK ${colMap.key} is processed."
                        )
                        val cols = colMap.value
                        cols.forEach { col ->
                            processingEnv.messager.printMessage(
                                Diagnostic.Kind.WARNING,
                                "- ${col.name} ${col.typeCollection} ${col.typeField} is processed."
                            )
                        }
                    }

                metaEntity.fields.forEach { println(it.typeCollection) }
            }.getOrElse {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "ERROR: ${it.message}")
            }


        }
        return true
    }

    private fun collectMetaForeignKey(
        elementsAnnotatedWith: List<Pair<ForeignKey, ModelClassName>>,
        entities: Map<ModelClassName, MetaEntity>,
        collector: Set<MetaForeignKey> = setOf()
    ) {

        val map = when (elementsAnnotatedWith.isEmpty()) {
            true -> collector
            false -> {
                val head = elementsAnnotatedWith.first()
                val foreignKey = head.first
                val currentClass = head.second
                val foreignClass = ModelClassName(foreignKey.kClass)
                val fromCols = metaEntityColumns(entities, currentClass, foreignKey.currentTypeCols)

                val toCols = metaEntityColumns(
                    entities = entities,
                    entity = foreignClass,
                    cols = foreignKey.outTypeCols
                )

                if (fromCols.size != toCols.size) {
                    error("Не совпадают по кол-ву списки колонок currentTypeCols и outTypeCols во внешнем ключе $foreignKey")
                }

                val map = fromCols.indices
                    .forEach { idx ->
                        val fromMetaEntityColumn = fromCols[idx]
                        val toMetaEntityColumn = toCols[idx]
                        if (fromMetaEntityColumn.type != toMetaEntityColumn.type) {
                            error("Для внешнего ключа $foreignKey не совпадают типы колонок  в текущей(${fromMetaEntityColumn.name}, ${fromMetaEntityColumn.type}) и внешней(${toMetaEntityColumn.name}, ${toMetaEntityColumn.type}) таблице")
                        }
                    }

                val foreignMetaEntity = entities[foreignClass]!!
                val uks = foreignMetaEntity.uniqueKeysFields
                    .filter { uksEntry ->

                        val ukCols = uksEntry.value.map { it.name }
                        val fkCols = toCols.map { it.name }
                        val b = ukCols.minus(fkCols).isEmpty() && fkCols.minus(ukCols).isEmpty()
                        b
                    }
                if (uks.size != 1) {
                    error("Для внешнего ключа $foreignKey во внешней таблице не найден уникальный ключ")
                }
                val element = MetaForeignKey(entities[currentClass]!!, foreignMetaEntity)
                val plus = collector.plus(element)
                plus
            }
        }


//        TODO("Not yet implemented")
    }

    private fun metaEntityColumns(
        entities: Map<ModelClassName, MetaEntity>,
        entity: ModelClassName,
        cols: Array<String>
    ): List<MetaEntityColumn> {
        val fromMetaEntity =
            entities[entity] ?: error("Не найдена сущность в контексте ${entity.value}")
        val fromCols = cols.map { fkField ->
            fromMetaEntity.fields.filter { field -> field.name == fkField }.firstOrNull()
                ?: error("Не найдено поле ${fkField}  внешнего ключа для сущности  ${entity.value}")
        }
        return fromCols
    }

    private fun collectMetaEntity(
        elementsAnnotatedWith: Set<Element>,
        collector: Map<Element, MetaEntity> = mapOf()
    ): Map<Element, MetaEntity> {


        val map = when (elementsAnnotatedWith.isEmpty()) {
            true -> collector
            false -> {
                val head = elementsAnnotatedWith.first()
                val tailElementsAnnotatedWith = elementsAnnotatedWith.drop(1).toSet()
                if (collector.contains(head)) {
                    collectMetaEntity(tailElementsAnnotatedWith, collector)
                } else {
                    val plus = collector.plus(head to MetaEntity(head))
                    collectMetaEntity(tailElementsAnnotatedWith, plus)
                }
            }

        }


        return map
    }

    companion object {
        internal fun Element.isKotlinElement() = getAnnotation(KOTLIN_METADATA_ANNOTATION) != null
        val KOTLIN_METADATA_CLASS = Class.forName("kotlin.Metadata")
        val KOTLIN_METADATA_ANNOTATION = KOTLIN_METADATA_CLASS.asSubclass(Annotation::class.java)
    }
}