package ru.vood.processor.datamodel.abstraction.model

import ru.vood.processor.datamodel.abstraction.model.abstraction.IGeneratedField
import javax.lang.model.element.Element

class MetaEntityColumn(
    override val element: Element
    ): IGeneratedField {
    override fun isNullable(): Boolean {
        return false
    }
}