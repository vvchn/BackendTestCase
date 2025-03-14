package com.example

import com.example.data.DatabaseConfig
import com.example.services.DictionaryService
import com.example.services.DictionaryServiceImpl
import controllers.dictionaries.DictionariesController
import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.OutputFormat
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val dictService: DictionaryService = DictionaryServiceImpl(DatabaseConfig.db)
    val controller = DictionariesController(dictService)

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(OpenApi) {
        outputFormat = OutputFormat.JSON

        server {
            url = ""
            description = "Локальный сервер"
        }

        info {
            title = "Тестовое задание для разработчика ПО"
            description =
                "Бэкенд-приложение, которое представляет собой универсальное решение для выполнения CRUD-операций над справочниками и их данными."
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