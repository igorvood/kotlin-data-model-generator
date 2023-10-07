package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment

class EntityEnumGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractGenerator<Set<MetaEntity>>(messager, filer, processingEnv) {

    override val subDir: String
        get() = "metaEnum"

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val commonPackage: String = "ru.vood.datamodel.meta.enums"//calculatePackage(packages)

                val entities = generatedClassData
                    .map { it.shortName }
                    .sorted()
                    .joinToString(",\n")
                val trimIndent = """
            package $commonPackage
            enum class MetaEntityEnum {
            $entities
            }
        """.trimIndent()
                setOf(GeneratedFile(FileName("MetaEntityEnum"), GeneratedCode(trimIndent)))
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

enum class asd {
    asds,
    asdsd
}