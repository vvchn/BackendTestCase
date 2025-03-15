package com.example.services

import com.example.models.DictionaryStructure
import com.example.models.FieldType

fun validateRawData(structure: DictionaryStructure, data: Map<String, String>): Boolean {
    val fieldMap = structure.fields.associateBy { it.name }

    structure.fields.forEach { field ->
        if (!data.containsKey(field.name)) {
            return false
        }
    }

    return data.all { (key, value) ->
        val field = fieldMap[key] ?: return false
        when (field.type) {
            FieldType.string -> true
            FieldType.number -> value.toIntOrNull() != null
            FieldType.double -> value.toDoubleOrNull() != null
            FieldType.boolean -> value.toBooleanStrictOrNull() != null
        }
    }
}