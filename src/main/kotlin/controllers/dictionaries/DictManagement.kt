package controllers.dictionaries

import com.example.models.DictionaryField
import com.example.models.DictionaryRequest
import com.example.models.DictionaryStructure
import com.example.models.FieldType
import com.example.services.DictionaryService
import io.github.smiley4.ktoropenapi.config.RouteConfig
import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

// TODO: Replace mock response with real ones
fun Route.dictManagement(service: DictionaryService) {
    route("/dictionaries") {
        get({
            description = "Получить список всех справочников"
            setTag()

            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<List<String>> {
                        example("default") {
                            value = listOf("products", "names", "test")
                        }
                    }
                }
            }
        }) {
            val dictionaries = service.getAllDictionaries()
            call.respond(dictionaries)
        }

        post({
            description = "Создать новый справочник. В теле запроса должны передаваться имя справочника и его структура (список полей и их типы)"
            setTag()

            request {
                body<DictionaryRequest> {
                    contentType(ContentType.Application.Json) {
                        example("default") {
                            value = DictionaryRequest(
                                name = "products",
                                structure = DictionaryStructure(
                                    fields = listOf(
                                        DictionaryField(name = "productName", type = FieldType.string),
                                        DictionaryField(name = "price", type = FieldType.number),
                                        DictionaryField(name = "inStock", type = FieldType.boolean)
                                    )
                                )
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
                            value = "Dictionary test created"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
                HttpStatusCode.Conflict to {
                    description = "Справочник с таким именем уже существует"
                }
            }
        }) {
            val request = call.receive<DictionaryRequest>()
            try {
                service.createDictionary(request)
                call.respond(HttpStatusCode.Created, "Dictionary ${request.name} created.")
            } catch (e: ExposedSQLException) {
                if (e.message?.contains("duplicate key") == true) {
                    call.respond(HttpStatusCode.Conflict, "Dictionary '${request.name}' already exists")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to create dictionary: ${e.message}")
                }
            }
        }

        post("/{fromName}/{toName}", {
            description = "Создать новый справочник, скопировав поля из другого справочника"
            setTag()

            request {
                pathParameter<String>("fromName") {
                    description = "Название существующего справочника, откуда будут копироваться данные"
                    required = true
                }
                pathParameter<String>("toName") {
                    description = "Название нового справочника"
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "Dictionary test2 created"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
                HttpStatusCode.Conflict to {
                    description = "Справочник с таким именем уже существует"
                }
            }
        }) {
            val fromName = call.parameters["fromName"]
            val toName = call.parameters["toName"]

            try {
                if (fromName.isNullOrBlank() || toName.isNullOrBlank()) {
                    throw IllegalArgumentException("Incorrect 'fromName' or 'toName' passed")
                }

                service.copyDictionary(fromName, toName)
                call.respond(HttpStatusCode.Created, "Dictionary $toName created")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "${e.message}")
            } catch (e: ExposedSQLException) {
                if (e.message?.contains("duplicate key") == true) {
                    call.respond(HttpStatusCode.Conflict, "Dictionary with name $fromName already exists")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to create dictionary: ${e.message}")
                }
            }
        }

        delete("/{name}", {
            description = "Удалить справочник и все его данные."
            setTag()

            request {
                pathParameter<String>("name") {
                    description = "Название существующего справочника, который нужно удалить"
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "DELETE /dictionaries: :TODO"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Справочник с таким именем не найдён"
                }
            }
        }) {
            val name = call.parameters["name"]

            return@delete if (name.isNullOrBlank()) {
                call.badRequest("Invalid argument \"name\"")
            } else {
                call.respondText("DELETE /$name: TODO")
            }
        }
    }
}

private inline fun RouteConfig.setTag() {
    tags = setOf("Управление справочниками")
}
