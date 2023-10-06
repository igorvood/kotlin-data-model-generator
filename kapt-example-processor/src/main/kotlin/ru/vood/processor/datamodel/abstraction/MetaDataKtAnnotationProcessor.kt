package ru.vood.processor.datamodel.abstraction

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.processor.datamodel.abstraction.model.*
import ru.vood.processor.datamodel.abstraction.model.abstracti.AbstractCommonGenerationProcessor
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("ru.vood.dmgen.annotation.FlowEntity")
class MetaDataKtAnnotationProcessor : AbstractCommonGenerationProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val metaInformation = roundEnv.metaInformation()
        println(metaInformation)


//        roundEnv.getElementsAnnotatedWith(FlowEntity::class.java).forEach { classElement ->
////            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "${classElement.simpleName} is processed.")
//            kotlin.runCatching {
//                classElement.asType().asTypeName()
//                val metaEntity = MetaEntity(classElement)
//                processingEnv.messager.printMessage(
//                    Diagnostic.Kind.WARNING,
//                    "${classElement.simpleName} ${classElement.isKotlinElement()} is processed."
//                )
//
//
//                metaEntity.uniqueKeysFields
//                    .forEach { colMap ->
//                        processingEnv.messager.printMessage(
//                            Diagnostic.Kind.WARNING,
//                            "- UK ${colMap.key} is processed."
//                        )
//                        val cols = colMap.value
//                        cols.forEach { col ->
//                            processingEnv.messager.printMessage(
//                                Diagnostic.Kind.WARNING,
//                                "- ${col.name} ${col.typeCollection} ${col.typeField} is processed."
//                            )
//                        }
//                    }
//
//                metaEntity.fields.forEach { println(it.typeCollection) }
//            }.getOrElse {
//                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "ERROR: ${it.message}")
//            }
//
//
//        }
        return true
    }








    companion object {
        internal fun Element.isKotlinElement() = getAnnotation(KOTLIN_METADATA_ANNOTATION) != null
        val KOTLIN_METADATA_CLASS = Class.forName("kotlin.Metadata")
        val KOTLIN_METADATA_ANNOTATION = KOTLIN_METADATA_CLASS.asSubclass(Annotation::class.java)
    }
}