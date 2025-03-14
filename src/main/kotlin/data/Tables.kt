package com.example.modules.data

import com.example.modules.models.DictionaryStructure
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json

object Dictionaries : Table() {
    val name = varchar("name", 50).uniqueIndex()
    val structure = json<DictionaryStructure>("structure", Json { ignoreUnknownKeys = true })
}

object DictionaryRecords : Table() {
    val id = integer("id").autoIncrement()
    val dictionaryName = varchar("dictionary_name", 50).references(Dictionaries.name)
    val data = text("data")
    override val primaryKey = PrimaryKey(id)
}