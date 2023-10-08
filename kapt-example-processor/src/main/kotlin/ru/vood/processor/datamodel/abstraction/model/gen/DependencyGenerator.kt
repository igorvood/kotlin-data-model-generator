package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class DependencyGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractDataDictionaryGenerator<Set<MetaEntity>>(messager, processingEnv) {

    override val nameClass: String
        get() = "Dependency"

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val trimIndent =
                    """package $commonPackage
                        
import $commonPackage.DataDictionaryEntityEnum
import $commonPackage.DataDictionaryForeignKeyEnum

object Dependency {

    val entityDependency : Map<${IMetaEntity::class.java.canonicalName}, Set<MetaDependency>> = collectDependency(DataDictionaryEntityEnum.values().toList(), DataDictionaryForeignKeyEnum.values().toList())


    private fun collectDependency(
        entities: List<${IMetaEntity::class.java.canonicalName}>,
        foreignKey: List<DataDictionaryForeignKeyEnum>
    ): Map<${IMetaEntity::class.java.canonicalName}, Set<MetaDependency>> {

        tailrec fun recursiveCollectDependency(
            values: List<DataDictionaryForeignKeyEnum>,
            collector: Map<${IMetaEntity::class.java.canonicalName}, Set<MetaDependency>>
        ): Map<${IMetaEntity::class.java.canonicalName}, Set<MetaDependency>> {
            return when (values.isEmpty()) {
                true -> collector
                false -> {
                    val dataDictionaryForeignKeyEnum = values[0]
                    val let = collector[dataDictionaryForeignKeyEnum.fromEntity]?.plus(
                        MetaDependency(dataDictionaryForeignKeyEnum.toEntity)
                    )
                        ?: setOf(MetaDependency(dataDictionaryForeignKeyEnum.toEntity))

                    val plus = collector.plus(dataDictionaryForeignKeyEnum.fromEntity to let)

                    val values1 = values.drop(1)
                    recursiveCollectDependency(values1, plus)
                }
            }
        }
        return recursiveCollectDependency(foreignKey, entities.associateWith { setOf() })
    }
}

data class MetaDependency(val toEntity: ${IMetaEntity::class.java.canonicalName})
"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }
}

