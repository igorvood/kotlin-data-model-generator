package ru.vood.processor.datamodel.abstraction

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("ru.vood.dmgen.annotation.FlowEntity")
class MetaDataKtAnnotationProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(FlowEntity::class.java)

        val allMeta:  Map<Element, MetaEntity> = collectMeta(elementsAnnotatedWith)


        val filter = elementsAnnotatedWith
            .filter { it.asType().asTypeName() is ClassName }




        val map: Map<Element, MetaEntity> = filter
            .map { it to MetaEntity(it) }
            .toMap()

        roundEnv.getElementsAnnotatedWith(FlowEntity::class.java).forEach { classElement ->
//            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "${classElement.simpleName} is processed.")
            kotlin.runCatching {
                classElement.asType().asTypeName()
                val metaEntity = MetaEntity(classElement)
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    "${classElement.simpleName} ${classElement.isKotlinElement()} is processed."
                )

                val fields = MetaEntity(classElement)
                    .fields
                    .forEach { col ->
                        processingEnv.messager.printMessage(
                            Diagnostic.Kind.WARNING,
                            "- ${col.name} ${col.typeCollection} ${col.typeField} is processed."
                        )
                    }

                metaEntity.fields.forEach { println(it.typeCollection) }
            }.getOrElse {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "ERROR: ${it.message}")
            }


        }
        return true
    }

    private fun collectMeta(elementsAnnotatedWith: Set<Element>, collector: Map<Element, MetaEntity> = mapOf()): Map<Element, MetaEntity> {



        val map = when (elementsAnnotatedWith.isEmpty()) {
            true -> collector
            false -> {
                val first = elementsAnnotatedWith.first()
                val elementsAnnotatedWith1 = elementsAnnotatedWith.drop(1).toSet()
                if (collector.contains(first)) {
                    collectMeta(elementsAnnotatedWith1, collector)
                } else {
                    val plus = collector.plus(first to MetaEntity(first))
                    collectMeta(elementsAnnotatedWith1, plus)
                }
            }

        }


        return  map
    }

    companion object {
        internal fun Element.isKotlinElement() = getAnnotation(KOTLIN_METADATA_ANNOTATION) != null
        val KOTLIN_METADATA_CLASS = Class.forName("kotlin.Metadata")
        val KOTLIN_METADATA_ANNOTATION = KOTLIN_METADATA_CLASS.asSubclass(Annotation::class.java)
    }
}