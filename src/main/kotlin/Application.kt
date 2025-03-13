package com.example

import com.example.controllers.DictionariesController
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val controller = DictionariesController(null)

    routing {
        with(controller) {
            configureRouting()
        }
    }
}