package com.example.data

import com.example.models.DictionaryStructure
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json

const val MAX_NAME_LENGTH = 50

object Dictionaries : Table() {
    val name = varchar("name", MAX_NAME_LENGTH).uniqueIndex()
    val structure = json<DictionaryStructure>("structure", Json { ignoreUnknownKeys = true })
}

object DictionaryRecords : Table() {
    val id = integer("id").autoIncrement()
    val dictionaryName = varchar("dictionary_name", MAX_NAME_LENGTH).references(Dictionaries.name)
    val data = text("data")
    override val primaryKey = PrimaryKey(id)
}