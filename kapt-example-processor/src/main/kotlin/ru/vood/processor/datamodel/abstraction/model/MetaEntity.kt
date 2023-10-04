package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Uk
import ru.vood.processor.datamodel.abstraction.model.abstraction.AbstractAnnotatedClass
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import javax.lang.model.element.Element

class MetaEntity(element: Element) : AbstractAnnotatedClass<MetaEntityColumn>(element) {
    override fun elementToIGeneratedField(e: Element): MetaEntityColumn =
        MetaEntityColumn(e)

    val flowEntity = element.annotation<FlowEntity>()

    val foreignKeys = element.annotations<ForeignKey>()

    val uniqueKeys = element.annotations<Uk>()
}