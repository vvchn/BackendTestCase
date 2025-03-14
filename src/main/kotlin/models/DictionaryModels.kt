package com.example.modules.models

import kotlinx.serialization.Serializable

@Serializable
enum class FieldType {
    STRING, NUMBER, BOOLEAN, DOUBLE
}

@Serializable
data class DictionaryField(
    val name: String,
    val type: FieldType,
)

@Serializable
data class DictionaryStructure(
    val fields: List<DictionaryField>
)

@Serializable
data class DictionaryRequest(
    val name: String,
    val structure: DictionaryStructure
)