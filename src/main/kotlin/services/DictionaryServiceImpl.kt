package com.example.services

import com.example.data.Dictionaries
import com.example.data.DictionaryRecords
import com.example.models.DictionaryRequest
import com.example.models.RecordResponse
import io.ktor.server.plugins.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DictionaryServiceImpl(private val db: Database) : DictionaryService {
    override suspend fun getAllDictionaries(): List<String> {
        return newSuspendedTransaction(db = db) {
            Dictionaries.selectAll().map { it[Dictionaries.name] }
        }
    }

    override suspend fun createDictionary(request: DictionaryRequest) {
        newSuspendedTransaction(db = db) {
            Dictionaries.insert {
                it[name] = request.name
                it[structure] = request.structure
            }
        }
    }

    override suspend fun copyDictionary(fromName: String, toName: String) {
        newSuspendedTransaction(db = db) {
            val srcStructure = Dictionaries.select(Dictionaries.name eq fromName)
                .singleOrNull()?.get(Dictionaries.structure)
                ?: throw NotFoundException("Source dictionary '$fromName' not found")

            if (Dictionaries.select(Dictionaries.name eq toName).count() > 0) {
                throw IllegalArgumentException("Dictionary '$toName' already exists")
            }

            Dictionaries.insert {
                it[name] = toName
                it[structure] = srcStructure
            }

            val records = DictionaryRecords.select(DictionaryRecords.dictionaryName eq fromName)
            records.forEach { record ->
                DictionaryRecords.insert {
                    it[dictionaryName] = toName
                    it[data] = record[data]
                }
            }
        }
    }

    override suspend fun deleteDictionary(name: String) {
        newSuspendedTransaction(db = db) {
            DictionaryRecords.deleteWhere { dictionaryName eq name }
            val dictDeleted = Dictionaries.deleteWhere { Dictionaries.name eq name }
            if (dictDeleted == 0) {
                throw NotFoundException("Dictionary '$name' not found")
            }
        }
    }

    override suspend fun getRecords(name: String): List<RecordResponse> {
        return newSuspendedTransaction(db = db) {
            Dictionaries.selectAll()
                .where { Dictionaries.name eq name }
                .singleOrNull()
                ?: throw NotFoundException("Dictionary '$name' not found")

            DictionaryRecords.selectAll()
                .where { DictionaryRecords.dictionaryName eq name }
                .map { row ->
                    val dataMap = Json.decodeFromString<Map<String, JsonElement>>(row[DictionaryRecords.data])
                    RecordResponse(
                        id = row[DictionaryRecords.id],
                        data = dataMap
                    )
                }
        }
    }

    override suspend fun addRecord(name: String, rawData: Map<String, JsonElement>) {
        newSuspendedTransaction(db = db) {
            val d = Dictionaries.selectAll()
                .where { Dictionaries.name eq name }
                .singleOrNull()
                ?: throw NotFoundException("Dictionary '$name' not found")

            val structure = d[Dictionaries.structure]

            val recordData = rawData.mapValues { (_, value) ->
                when (value) {
                    is JsonPrimitive -> value.content
                    else -> value.toString()
                }
            }

            require(validateRawData(structure, recordData)) {
                "Incorrect data passed"
            }

            val jsonData = Json.encodeToString(recordData)

            DictionaryRecords.insert {
                it[dictionaryName] = name
                it[data] = jsonData
            }
        }
    }

    override suspend fun getRecord(name: String, id: Int): Pair<Int, String>? {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecord(name: String, id: Int, data: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecord(name: String, id: Int) {
        TODO("Not yet implemented")
    }

}