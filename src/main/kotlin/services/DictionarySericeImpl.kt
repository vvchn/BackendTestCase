package com.example.services

import com.example.models.DictionaryRequest
import org.jetbrains.exposed.sql.Database

class DictionaryServiceImpl(private val db: Database): DictionaryService {
    override suspend fun getAllDictionaries(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun createDictionary(request: DictionaryRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun copyDictionary(fromName: String, toName: String) {
        TODO("Not yet implemented")
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