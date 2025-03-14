package com.example.models

import kotlinx.serialization.Serializable

@Suppress("EnumEntryName")
@Serializable
enum class FieldType {
    string, number, boolean, double
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