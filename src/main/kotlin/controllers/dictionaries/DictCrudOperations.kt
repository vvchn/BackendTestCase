package controllers.dictionaries

import com.example.models.RecordResponse
import com.example.services.DictionaryService
import io.github.smiley4.ktoropenapi.config.RouteConfig
import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.github.smiley4.ktoropenapi.put
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.dictCrudOperations(service: DictionaryService) {
    route("/dictionaries/{name}/records") {
        get({
            description = "Получить список всех записей в справочнике"
            setTag()

            request {
                pathParameter<String>("name") {
                    description = "Название существующего справочника"
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<RecordResponse> {
                        contentType(ContentType.Application.Json) {
                            example("default") {
                                value = RecordResponse(
                                    1,
                                    Json.decodeFromString<Map<String, JsonElement>>(
                                        "{\"id\":1,\"data\":{\"productName\":\"Example Product\",\"price\":\"19\",\"inStock\":\"true\"}}"
                                    )
                                )
                            }
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
                HttpStatusCode.NotFound to {
                    description = "Справочник с таким именем не найдён"
                }
            }
        }) {
            val name = call.parameters["name"]

            try {
                if (name.isNullOrBlank()) {
                    throw IllegalArgumentException("Incorrect 'name' passed")
                }

                call.respond(service.getRecords(name))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message.orEmpty())
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.orEmpty())
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to get records")
            }
        }

        post({
            description = "Добавить новую запись в справочник"
            setTag()

            request {
                pathParameter<String>("name") {
                    description = "Название существующего справочника"
                    required = true
                }
                body<Map<String, JsonElement>> {
                    contentType(ContentType.Application.Json) {
                        example("default") {
                            value = mapOf(
                                "productName" to "Example Product",
                                "price" to 19.99,
                                "inStock" to true
                            )
                        }
                    }
                }
            }
            response {
                HttpStatusCode.Created to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "New entry for 'test' added successfully"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
                HttpStatusCode.NotFound to {
                    description = "Справочник с таким именем не найдён"
                }
            }
        }) {
            val name = call.parameters["name"]

            try {
                if (name.isNullOrBlank()) {
                    throw IllegalArgumentException("Incorrect 'name' passed")
                }

                val recordData = call.receive<Map<String, JsonElement>>()
                service.addRecord(name, recordData)

                call.respond(HttpStatusCode.Created, "New entry for '$name' added successfully")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message.orEmpty())
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.orEmpty())
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to add records")
            }
        }
    }

    route("/dictionaries/{name}/records/{id}") {
        get({
            description = "Получить информацию о конкретной записи"
            setTag()

            request {
                pathParameter<String>("name") {
                    description = "Название существующего справочника"
                    required = true
                }
                pathParameter<String>("id") {
                    description = "ID записи"
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<RecordResponse> {
                        contentType(ContentType.Application.Json) {
                            example("default") {
                                value = RecordResponse(
                                    1,
                                    Json.decodeFromString<Map<String, JsonElement>>(
                                        "{\"id\":1,\"data\":{\"productName\":\"Example Product\",\"price\":\"19\",\"inStock\":\"true\"}}"
                                    )
                                )
                            }
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
                HttpStatusCode.NotFound to {
                    description = "Справочник или запись не найдена"
                }
            }
        }) {
            val name = call.parameters["name"]
            val id = call.parameters["id"]

            try {
                if (name.isNullOrBlank() || id.isNullOrBlank()) {
                    throw IllegalArgumentException("Incorrect 'name' or 'id' passed")
                }

                requireNotNull(id.toIntOrNull()) {
                    "Incorrect 'id' passed"
                }

                call.respond(HttpStatusCode.OK, service.getRecord(name, id.toInt()))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message.orEmpty())
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.orEmpty())
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to add records")
            }
        }

        put({
            description = "Обновить информацию о конкретной записи"
            setTag()

            request {
                pathParameter<String>("name") {
                    description = "Название существующего справочника"
                    required = true
                }
                pathParameter<String>("id") {
                    description = "ID записи"
                    required = true
                }
                body<Map<String, JsonElement>> {
                    contentType(ContentType.Application.Json) {
                        example("default") {
                            value = mapOf(
                                "productName" to "Brand New Example Product",
                                "price" to 29.99,
                                "inStock" to true
                            )
                        }
                    }
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "The record successfully updated"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
                HttpStatusCode.NotFound to {
                    description = "Справочник или запись не найдена"
                }
            }
        }) {
            val name = call.parameters["name"]
            val id = call.parameters["id"]

            try {
                if (name.isNullOrBlank() || id.isNullOrBlank()) {
                    throw IllegalArgumentException("Incorrect 'name' or 'id' passed")
                }

                requireNotNull(id.toIntOrNull()) {
                    "Incorrect 'id' passed"
                }

                val recordData = call.receive<Map<String, JsonElement>>()
                service.updateRecord(name, id.toInt(), recordData)

                call.respond(HttpStatusCode.OK, "The record successfully updated")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message.orEmpty())
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.orEmpty())
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to add records")
            }
        }

        delete({
            description = "Удалить запись из справочника"
            setTag()

            request {
                pathParameter<String>("name") {
                    description = "Название существующего справочника"
                    required = true
                }
                pathParameter<String>("id") {
                    description = "ID записи, которую нужно удалить"
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "The record successfully deleted"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
                HttpStatusCode.NotFound to {
                    description = "Справочник или запись не найдена"
                }
            }
        }) {
            val name = call.parameters["name"]
            val id = call.parameters["id"]

            try {
                if (name.isNullOrBlank() || id.isNullOrBlank()) {
                    throw IllegalArgumentException("Incorrect 'name' or 'id' passed")
                }

                requireNotNull(id.toIntOrNull()) {
                    "Incorrect 'id' passed"
                }

                service.deleteRecord(name, id.toInt())

                call.respond(HttpStatusCode.OK, "The record successfully deleted")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message.orEmpty())
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.orEmpty())
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to add records")
            }
        }
    }
}

private inline fun RouteConfig.setTag() {
    tags = setOf("CRUD-операции над данными справочников")
}