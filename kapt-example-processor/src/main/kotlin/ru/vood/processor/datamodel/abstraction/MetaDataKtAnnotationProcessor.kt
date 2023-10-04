package ru.vood.processor.datamodel.abstraction

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaForeignKey
import ru.vood.processor.datamodel.abstraction.model.ModelClassName
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import ru.vood.processor.datamodel.abstraction.model.abstraction.proxyAnnotationValue
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


        val entities = allMeta.map { ModelClassName(it.value.kotlinMetaClass.toString()) to it.value }.toMap()

        val elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(ForeignKey::class.java)

        collectMetaForeignKey(elementsAnnotatedWith, entities)


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
        elementsAnnotatedWith: Set<Element>,
        entities: Map<ModelClassName, MetaEntity>,
        collector: Map<ClassName, Set<MetaForeignKey>> = mapOf()
    ) {

        val map = when (elementsAnnotatedWith.isEmpty()) {
            true -> collector
            false -> {
                val head = elementsAnnotatedWith.first()
                val tailElementsAnnotatedWith = elementsAnnotatedWith.drop(1).toSet()
                val annotations = head.annotations<ForeignKey>()

                val map = annotations
                    .map {
                        val annotationValue = head.proxyAnnotationValue<ForeignKey, Any>(processingEnv, "kClass")
                        val orElseThrow = annotationValue.orElseThrow()
                        val orElseThrow1 = orElseThrow.toString()
                        val toClassName = head.asType().asTypeName() as ClassName
                        val canonicalName = toClassName.canonicalName
//                        val qualifiedName = it.kClass.qualifiedName
                        val metaForeignKey = MetaForeignKey(
//                            toClassName
                        )


                        toClassName to metaForeignKey
                    }


                collector
            }
        }


//        TODO("Not yet implemented")
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