package com.example.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

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

@Serializable
data class RecordResponse(
    val id: Int,
    val data: Map<String, JsonElement>
)