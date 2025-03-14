package com.example.services

import com.example.models.DictionaryRequest

interface DictionaryService {
    // Операции со справочниками
    suspend fun getAllDictionaries(): List<String>
    suspend fun createDictionary(request: DictionaryRequest)
    suspend fun copyDictionary(fromName: String, toName: String)
    suspend fun deleteDictionary(name: String)

    // Операции с записями
    suspend fun getRecords(name: String): List<Pair<Int, String>>
    suspend fun addRecord(name: String, data: String)
    suspend fun getRecord(name: String, id: Int): Pair<Int, String>?
    suspend fun updateRecord(name: String, id: Int, data: String)
    suspend fun deleteRecord(name: String, id: Int)
}