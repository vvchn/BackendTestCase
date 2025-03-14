package com.example.services

import com.example.data.Dictionaries
import com.example.models.DictionaryRequest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
                ?: throw IllegalArgumentException("Source dictionary '$fromName' not found")
            Dictionaries.insert {
                it[name] = toName
                it[structure] = srcStructure
            }
        }
    }

    override suspend fun deleteDictionary(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRecords(name: String): List<Pair<Int, String>> {
        TODO("Not yet implemented")
    }

    override suspend fun addRecord(name: String, data: String) {
        TODO("Not yet implemented")
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