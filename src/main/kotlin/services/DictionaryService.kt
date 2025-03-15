package com.example.services

import com.example.models.DictionaryRequest
import com.example.models.RecordResponse
import kotlinx.serialization.json.JsonElement

interface DictionaryService {
    // Операции со справочниками
    suspend fun getAllDictionaries(): List<String>
    suspend fun createDictionary(request: DictionaryRequest)
    suspend fun copyDictionary(fromName: String, toName: String)
    suspend fun deleteDictionary(name: String)

    // Операции с записями
    suspend fun getRecords(name: String): List<RecordResponse>
    suspend fun addRecord(name: String, rawData: Map<String, JsonElement>)
    suspend fun getRecord(name: String, id: Int): RecordResponse
    suspend fun updateRecord(name: String, idToUpdate: Int, rawData: Map<String, JsonElement>)
    suspend fun deleteRecord(name: String, id: Int)
}