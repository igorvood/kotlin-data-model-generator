package ru.vood.processor.datamodel.abstraction

import ru.vood.processor.datamodel.abstraction.model.abstraction.AbstractGeneratedClass
import java.io.OutputStreamWriter
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

abstract class AbstractGenerationProcessor<GeneratedClass : AbstractGeneratedClass<*>> :
    AbstractCommonGenerationProcessor<GeneratedClass>() {

    abstract fun generatedClassInfo(typeElement: TypeElement): GeneratedClass

    abstract fun textGenerator(generatedClassData: GeneratedClass): String

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val flatMap = annotations
            .flatMap { annotation -> roundEnv.getElementsAnnotatedWith(annotation) }
        val filterIsInstance = flatMap
            .filterIsInstance<TypeElement>()
        filterIsInstance
            .map { generateBy ->
                val generatedClass = generatedClassInfo(generateBy)
                val out = processingEnv.filer
                    .createSourceFile(generatedClass.fullGeneratedName())
                    .let { OutputStreamWriter(it.openOutputStream()) }

                log(
                    Diagnostic.Kind.MANDATORY_WARNING,
                    "Generate class ${generatedClass.generatedClassName()} by class ${generatedClass.annotatedClass.name()}"
                )

                out.write(textGenerator(generatedClass))
                out.close()
            }
        return true
    }

}