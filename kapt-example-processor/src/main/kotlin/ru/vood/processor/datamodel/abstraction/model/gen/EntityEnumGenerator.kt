package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class EntityEnumGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractDataDictionaryGenerator<Set<MetaEntity>>(messager, filer, processingEnv) {

    override val nameClass: String
        get() = "DataDictionaryEntityEnum"

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val entities = generatedClassData
                    .map { it.shortName+"(${it.kotlinMetaClass.canonicalName}::class)" }
                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package $commonPackage
import kotlin.reflect.KClass

enum class $nameClass(
val designkClass: KClass<*>
) {
$entities
}
"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }

    private fun calculatePackage(packages: List<String>): String {
        fun d(packages: List<String>, currentPackage: String): String {
            when (packages.size) {
                0 -> currentPackage
                else -> {
                    val nextPackage = packages[0]

                    currentPackage.withIndex()
                        .map {

                        }


                    currentPackage
                }
            }


            TODO()
        }



        return when (packages.size) {
            0 -> error("Why package size is empty?")
            1 -> packages[0]
            else -> d(packages.drop(1), packages[0])
        }
    }
}

