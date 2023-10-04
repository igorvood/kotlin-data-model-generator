package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.processor.datamodel.abstraction.model.abstraction.AbstractAnnotatedClass
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import javax.lang.model.element.Element

class MetaEntity(element: Element): AbstractAnnotatedClass<MetaEntityColumn>(element) {

    override fun elementToIGeneratedField(e: Element): MetaEntityColumn =
        MetaEntityColumn(e)

    val flowEntity by lazy { element.annotation<FlowEntity>() }

    val foreignKeys by lazy { element.annotations<ForeignKey>() }
}