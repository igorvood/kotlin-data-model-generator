package ru.vood.processor.datamodel.abstraction

import com.google.auto.service.AutoService
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
        metaInformation.entities
            .forEach { entity ->
                with(entity.key.value) {
                    log(Diagnostic.Kind.NOTE, "Collect meta for $this")
                }
                with(entity.value.fields) {
                    forEach { field ->
                        log(
                            Diagnostic.Kind.NOTE,
                            "Field(name, type, collection type) - ${field.name}, ${field.type}, ${field.typeCollection} "
                        )
                    }
                }

                println(metaInformation)

            }
                return true

    }


    companion object {
        internal fun Element.isKotlinElement() = getAnnotation(KOTLIN_METADATA_ANNOTATION) != null
        val KOTLIN_METADATA_CLASS = Class.forName("kotlin.Metadata")
        val KOTLIN_METADATA_ANNOTATION = KOTLIN_METADATA_CLASS.asSubclass(Annotation::class.java)
    }
}
