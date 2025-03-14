package controllers.dictionaries

import io.github.smiley4.ktoropenapi.config.RouteConfig
import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// TODO: Replace mock response with real ones
fun Route.dictManagement() {
    route("/dictionaries") {
        get({
            description = "Получить список всех справочников"
            setTag()

            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "GET /dictionaries: TODO"
                        }
                    }
                }
            }
        }) {
            call.respondText("GET /dictionaries: TODO")
        }

        post({
            description = "Создать новый справочник. В теле запроса должны передаваться имя справочника и его структура (список полей и их типы)"
            setTag()

            response {
                HttpStatusCode.OK to {
                    description = "Успешное выполнение"
                    body<String> {
                        example("default") {
                            value = "POST /dictionaries: :TODO"
                        }
                    }
                }
            }
        }) {
            call.respondText("POST /dictionaries: :TODO")
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
                            value = "POST /dictionaries/fromName/toName: TODO"
                        }
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Переданы некорректные аргументы"
                }
            }
        }) {
            val fromName = call.parameters["fromName"]
            val toName = call.parameters["toName"]

            when {
                fromName.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"fromName\"")
                    return@post
                }

                toName.isNullOrBlank() -> {
                    call.badRequest("Invalid argument \"toName\"")
                    return@post
                }

                else -> {
                    call.respondText("POST /dictionaries/$fromName/$toName: TODO")
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