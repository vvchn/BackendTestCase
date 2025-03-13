package com.example

import com.example.controllers.dictionaries.DictionariesController
import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.OutputFormat
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorswaggerui.swaggerUI
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

    install(OpenApi) {
        outputFormat = OutputFormat.JSON

        server {
            url = ""
            description = "Локальный сервер"
        }

        info {
            title = "Тестовое задание для разработчика ПО"
            description = "Бэкенд-приложение, которое представляет собой универсальное решение для выполнения CRUD-операций над справочниками и их данными."
        }
    }

    routing {
        route("swagger") {
            swaggerUI("/api.json") {
                showExtensions = true
                showCommonExtensions = true
            }
        }
        route("api.json") { openApi() }

        with(controller) {
            configureRouting()
        }
    }
}