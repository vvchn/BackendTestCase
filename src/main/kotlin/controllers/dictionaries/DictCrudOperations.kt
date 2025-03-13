package com.example.controllers.dictionaries

import io.github.smiley4.ktoropenapi.config.RouteConfig
import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.github.smiley4.ktoropenapi.put
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// TODO: Replace mock response with real ones
fun Route.dictCrudOperations() {
    route("/dictionaries/{name}/records") {
        val errMsg = "Invalid argument \"name\""

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
                    body<String> {
                        example("default") {
                            value = "GET /dictionaries/name/records: TODO"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Справочник с таким именем не найдён"
                }
            }
        }) {
            val name = call.parameters["name"]

            return@get if (name.isNullOrBlank()) {
                call.badRequest(errMsg)
            } else {
                call.respondText("GET /dictionaries/$name/records: TODO")
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
            }
            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "POST /dictionaries/name/records: TODO"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Справочник с таким именем не найдён"
                }
            }
        }) {
            val name = call.parameters["name"]

            return@post if (name.isNullOrBlank()) {
                call.badRequest(errMsg)
            } else {
                call.respondText("POST /dictionaries/$name/records: TODO")
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
                    body<String> {
                        example("default") {
                            value = "POST /dictionaries/name/records: TODO"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Справочник или запись не найдена"
                }
            }
        }) {
            val name = call.parameters["name"]
            val id = call.parameters["id"]

            when {
                name.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"name\"")
                    return@get
                }

                id.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"id\"")
                    return@get
                }

                else -> call.respondText("GET /dictionaries/$name/records/$id: TODO")
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
            }

            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "PUT /dictionaries/name/records: TODO"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Справочник с таким именем не найден"
                }
            }

        }) {
            val name = call.parameters["name"]
            val id = call.parameters["id"]

            when {
                name.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"name\"")
                    return@put
                }

                id.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"id\"")
                    return@put
                }

                else -> call.respondText("PUT /dictionaries/$name/records/$id: TODO")
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
                            value = "DELETE /dictionaries/name/records: TODO"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Справочник или запись не найдена"
                }
            }
        }) {
            val name = call.parameters["name"]
            val id = call.parameters["id"]

            when {
                name.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"name\"")
                    return@delete
                }

                id.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"id\"")
                    return@delete
                }

                else -> call.respondText("DELETE /dictionaries/$name/records/$id: TODO")
            }

        }
    }
}

private inline fun RouteConfig.setTag() {
    tags = setOf("CRUD-операции над данными справочников")
}