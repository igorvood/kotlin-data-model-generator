package ru.vood.dmgen.annotation

enum class RelationType(val mandatory: Boolean) {
    MANY_TO_ONE(true),
    UNNOWN(true),
    ONE_TO_ONE_MANDATORY(true),
    ONE_TO_ONE_OPTIONAL(false);


}