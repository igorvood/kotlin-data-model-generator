package ru.vood.processor.datamodel.abstraction.model

import ru.vood.processor.datamodel.abstraction.model.abstraction.AbstractAnnotatedClass
import javax.lang.model.element.Element

class MetaEntity(element: Element): AbstractAnnotatedClass<MetaEntityColumn>(element) {

    override fun elementToIGeneratedField(e: Element): MetaEntityColumn =
        MetaEntityColumn(e)
}