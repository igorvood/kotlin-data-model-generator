package ru.vood.processor.datamodel.abstraction.model

data class MetaInformation(
    val collectMetaForeignKey: Set<MetaForeignKey>,
    val entities: Map<ModelClassName, MetaEntity>) {


}

