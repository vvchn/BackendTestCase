package com.example.controllers

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// TODO: Service, requests
class DictionariesController(private val service: Nothing?) {
    fun Routing.configureRouting() {
        route("/dictionaries") {
            get {
                call.respondText("GET /dictionaries: TODO")
            }

            post {
                call.respondText("POST /dictionaries: :TODO")
            }

            post("/{fromName}/{toName}") {
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
                        call.respondText("POST /$fromName/$toName: TODO")
                    }
                }
            }

            delete("/{name}") {
                val name = call.parameters["name"]

                return@delete if (name.isNullOrBlank()) {
                    call.badRequest("Invalid argument \"name\"")
                } else {
                    call.respondText("DELETE /$name: TODO")
                }
            }
        }

        route("/dictionaries/{name}/records") {
            val errMsg = "Invalid argument \"name\""

            get {
                val name = call.parameters["name"]

                return@get if (name.isNullOrBlank()) {
                    call.badRequest(errMsg)
                } else {
                    call.respondText("GET /dictionaries/$name/records: TODO")
                }
            }

            post {
                val name = call.parameters["name"]

                return@post if (name.isNullOrBlank()) {
                    call.badRequest(errMsg)
                } else {
                    call.respondText("POST /dictionaries/$name/records: TODO")
                }
            }
        }

        route("/dictionaries/{name}/records/{id}") {
            get {
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
                }
            }

            put {
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
                }
            }

            delete {
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
                }
            }
        }
    }
}

private suspend fun RoutingCall.badRequest(msg: String) {
    respond(
        status = HttpStatusCode.BadRequest,
        message = mapOf("error" to msg)
    )
}