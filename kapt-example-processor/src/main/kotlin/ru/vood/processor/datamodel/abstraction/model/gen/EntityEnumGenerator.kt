package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager

class EntityEnumGenerator(
    messager: Messager,
    filer: Filer

): AbstractGenerator<MetaEntity>(messager, filer) {

    override fun textGenerator(generatedClassData: MetaEntity): String {







        TODO("Not yet implemented")
    }
}