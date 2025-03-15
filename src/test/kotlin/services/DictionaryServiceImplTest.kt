package services

import com.example.data.Dictionaries
import com.example.data.DictionaryRecords
import com.example.models.DictionaryField
import com.example.models.DictionaryRequest
import com.example.models.DictionaryStructure
import com.example.models.FieldType
import com.example.services.DictionaryServiceImpl
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DictionaryServiceImplTest {

    private lateinit var db: Database
    private lateinit var service: DictionaryServiceImpl

    @BeforeEach
    fun setup() {
        db = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        transaction(db) {
            SchemaUtils.drop(Dictionaries, DictionaryRecords)
            SchemaUtils.create(Dictionaries, DictionaryRecords)
        }
        service = DictionaryServiceImpl(db)
    }

    @Test
    fun testGetAllDictionariesEmpty() {
        runBlocking {
            val result = service.getAllDictionaries()
            assertEquals(emptyList<String>(), result)
        }
    }

    @Test
    fun testCreateDictionary() {
        runBlocking {
            val structure = DictionaryStructure(
                fields = listOf(
                    DictionaryField("field1", FieldType.string),
                    DictionaryField("field2", FieldType.number)
                )
            )
            val request = DictionaryRequest("testDict", structure)
            service.createDictionary(request)

            val dictionaries = service.getAllDictionaries()
            assertEquals(listOf("testDict"), dictionaries)
        }
    }

    @Test
    fun testDeleteDictionary() {
        runBlocking {
            val structure = DictionaryStructure(
                fields = listOf(DictionaryField("field", FieldType.string))
            )
            service.createDictionary(DictionaryRequest("toDelete", structure))

            service.deleteDictionary("toDelete")

            val dictionaries = service.getAllDictionaries()
            assertEquals(emptyList<String>(), dictionaries)
        }
    }

    @Test
    fun testAddRecord() {
        runBlocking {
            val structure = DictionaryStructure(
                fields = listOf(
                    DictionaryField("name", FieldType.string),
                    DictionaryField("age", FieldType.number)
                )
            )
            service.createDictionary(DictionaryRequest("dict", structure))

            val data = mapOf(
                "name" to JsonPrimitive("John"),
                "age" to JsonPrimitive("25")
            )

            service.addRecord("dict", data)

            val records = service.getRecords("dict")
            assertEquals(1, records.size)
            assertEquals(data, records[0].data)
        }
    }

    @Test
    fun testDeleteDictionaryRemovesRecords() {
        runBlocking {
            val structure = DictionaryStructure(
                fields = listOf(
                    DictionaryField("name", FieldType.string)
                )
            )
            service.createDictionary(DictionaryRequest("dict", structure))
            service.addRecord("dict", mapOf("name" to JsonPrimitive("John")))

            service.deleteDictionary("dict")

            val dictionaries = service.getAllDictionaries()
            assertEquals(emptyList<String>(), dictionaries)

            val records = transaction(db) {
                DictionaryRecords.selectAll().toList()
            }
            assertEquals(0, records.size)
        }
    }

    @Test
    fun testCopyDictionarySuccess() {
        runBlocking {
            val structure = DictionaryStructure(
                fields = listOf(
                    DictionaryField("name", FieldType.string),
                    DictionaryField("age", FieldType.number)
                )
            )
            service.createDictionary(DictionaryRequest("source", structure))
            service.addRecord("source", mapOf("name" to JsonPrimitive("John"), "age" to JsonPrimitive("25")))

            service.copyDictionary("source", "target")

            val dictionaries = service.getAllDictionaries()
            assertEquals(listOf("source", "target"), dictionaries.sorted())

            val sourceRecords = service.getRecords("source")
            val targetRecords = service.getRecords("target")
            assertEquals(1, targetRecords.size)
            assertEquals(sourceRecords[0].data, targetRecords[0].data)
        }
    }

    @Test
    fun testCopyEmptyDictionary() {
        runBlocking {
            val structure = DictionaryStructure(fields = listOf(DictionaryField("field", FieldType.string)))
            service.createDictionary(DictionaryRequest("source", structure))

            service.copyDictionary("source", "target")

            val dictionaries = service.getAllDictionaries()
            assertEquals(listOf("source", "target"), dictionaries.sorted())

            val targetRecords = service.getRecords("target")
            assertEquals(0, targetRecords.size)
        }
    }
}