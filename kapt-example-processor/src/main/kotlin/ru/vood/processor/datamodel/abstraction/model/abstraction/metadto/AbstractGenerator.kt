package ru.vood.processor.datamodel.abstraction.model.abstraction.metadto

abstract class AbstractGenerator<META> {
    abstract fun textGenerator(generatedClassData: META): String

}