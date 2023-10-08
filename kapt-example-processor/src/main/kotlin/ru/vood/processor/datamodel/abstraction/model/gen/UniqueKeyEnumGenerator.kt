package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.IMetaColumnEntity
import ru.vood.dmgen.intf.IMetaUkEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class UniqueKeyEnumGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractDataDictionaryGenerator<Set<MetaEntity>>(messager, filer, processingEnv) {

    override val nameClass: String
        get() = "DataDictionaryUniqueKeyEnum"

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val entities = generatedClassData
                    .flatMap { metaEnt ->
                        metaEnt.uniqueKeysFields.keys
                            .map { ukDto ->
                                val ukCols =
                                    ukDto.cols.map { columnName -> metaEnt.name + "_" + columnName.value }
                                        .sorted()
                                        .joinToString(",")

                                ukDto.name.value + "(setOf($ukCols))"

                            }
                    }

                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package $commonPackage
                        
import $commonPackage.DataDictionaryColumnEntityEnum.*

enum class $nameClass(
    override val columns: Set<${IMetaColumnEntity::class.java.canonicalName}>
): ${IMetaUkEntity::class.java.canonicalName}{
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

