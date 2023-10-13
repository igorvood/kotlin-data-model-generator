package ru.vood.processor.datamodel.abstraction.model.abstraction.metadto

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.asTypeName
import ru.vood.processor.datamodel.abstraction.model.ColumnName
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import java.util.*
import javax.lang.model.element.Element

abstract class AbstractField(val element: Element) {

    protected val kotlinMetaClass = fold({ it }, { it })
    protected inline fun <reified T> fold(
        classNameF: (ClassName) -> T,
        parameterizedTypeNameF: (ParameterizedTypeName) -> T,
    ): T {
        return when (val t = element.asType().asTypeName()) {
            is ClassName -> classNameF(t)
            is ParameterizedTypeName -> parameterizedTypeNameF(t)
            else -> error("unsupported class $t")
        }
    }

    val name = ColumnName(element.simpleName.toString())

    abstract fun isNullable(): Boolean

    val type: String = when(val t = kotlinMetaClass.toString()){
            "java.lang.Long" -> "Long"
        "java.lang.Double" -> "Double"
        "java.lang.Float" -> "Float"
        "java.lang.Boolean" -> "Boolean"
        "java.lang.String" -> "String"
        "java.lang.Integer" -> "Int"

        else -> t
    }

    val typeCollection by lazy {
        val fold = fold(
            { null },
            {
                when (val canonicalName = it.rawType.canonicalName) {
                    "java.util.Set" -> SupportedGenericType.SET
                    "java.util.List" -> SupportedGenericType.LIST
                    else -> error("unsupported generic type $canonicalName")
                }
            }
        )
        fold
    }

    val typeField = fold(
        { it },
        {
            it.typeArguments[0]
        }
    )

//    fun betterClass(): AbstractAnnotatedClass?
//    fun isUpdateble(): Boolean
}

inline fun <reified ANNO : Annotation> AbstractField.annotation(): Optional<ANNO> =
    element.annotation()


