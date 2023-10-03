package ru.vood.processor.datamodel.abstraction

import com.google.auto.service.AutoService
import kaptexample.annotation.SampleAnnotation
import ru.vood.dmgen.annotation.FlowEntity
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("ru.vood.dmgen.annotation.FlowEntity")
class MetaDataKtAnnotationProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(FlowEntity::class.java).forEach {
            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "${it.simpleName} is processed.")

        }
        return true
    }
}