package com.example.controllers.dictionaries

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// TODO: Service
class DictionariesController(private val service: Nothing?) {
    fun Routing.configureRouting() {
        dictManagement()
        dictCrudOperations()
    }
}

suspend inline fun RoutingCall.badRequest(msg: String) {
    respond(
        status = HttpStatusCode.BadRequest,
        message = mapOf("error" to msg)
    )
}