package ru.vood.processor.datamodel.abstraction

import com.google.auto.service.AutoService
import ru.vood.processor.datamodel.abstraction.model.*
import ru.vood.processor.datamodel.abstraction.model.abstraction.AbstractCommonGenerationProcessor
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator.Companion.KAPT_KOTLIN_GENERATED_OPTION_NAME
import ru.vood.processor.datamodel.abstraction.model.gen.*
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.ContextDataClassesGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.EntityDataClassesGenerator
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("ru.vood.dmgen.annotation.FlowEntity")
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
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

                with(entity.value.uniqueKeysFields) {
                    forEach { uk ->
                        log(
                            Diagnostic.Kind.NOTE,
                            "Field(name, type, collection type) - ${uk} "
                        )
                    }
                }


            }

        val setMetaEnt = metaInformation.entities.map { it.value }.toSet()
        if (setMetaEnt.isNotEmpty()) {
            val cmmomPackage: String = commonPackage(setMetaEnt)
            EntityEnumGenerator(messager, filer, processingEnv).createFiles(setMetaEnt)
            DependencyGenerator(messager, filer, processingEnv).createFiles(setMetaEnt)

            ColumnEntityEnumGenerator(messager, filer, processingEnv).createFiles(setMetaEnt)
            UniqueKeyEnumGenerator(messager, filer, processingEnv).createFiles(setMetaEnt)
            ForeignKeyEnumGenerator(messager, filer, processingEnv).createFiles(metaInformation.collectMetaForeignKey)

            ContextDataClassesGenerator(messager, filer, processingEnv).createFiles(setMetaEnt)
            EntityDataClassesGenerator(messager, filer, processingEnv).createFiles(setMetaEnt)
        }



        return true

    }

    fun commonPackage(setMetaEnt: Set<MetaEntity>): String {
        tailrec fun commonPackageRecurcive(currentPackage: String, packacges: List<String>):String{
            return when(packacges.isEmpty()){
                true -> currentPackage
                false -> {
                    val nextPack = packacges[0]

                    var collector= ""

                    for (q in nextPack.withIndex()){
                        if (currentPackage.getOrElse(q.index) { '~' } == q.value){
                            collector = collector.plus(q.value)
                        } else{
                            break
                        }


                    }

                    if (collector.isEmpty()){
                        println(1)
                    }


                    commonPackageRecurcive(collector, packacges.drop(1))
                }
            }

        }
        val toList = setMetaEnt.toList().map { it.kotlinMetaClass.toString() }
        val value = toList[0]
        return commonPackageRecurcive(value, toList.drop(1))
    }


    companion object {
        internal fun Element.isKotlinElement() = getAnnotation(KOTLIN_METADATA_ANNOTATION) != null
        val KOTLIN_METADATA_CLASS = Class.forName("kotlin.Metadata")
        val KOTLIN_METADATA_ANNOTATION = KOTLIN_METADATA_CLASS.asSubclass(Annotation::class.java)
    }
}
