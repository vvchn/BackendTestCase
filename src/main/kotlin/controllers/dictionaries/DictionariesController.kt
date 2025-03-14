package controllers.dictionaries

import com.example.services.DictionaryService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class DictionariesController(private val service: DictionaryService) {
    fun Routing.configureRouting() {
        dictManagement(service)
        dictCrudOperations(service)
    }
}

suspend inline fun RoutingCall.badRequest(msg: String) {
    respond(
        status = HttpStatusCode.BadRequest,
        message = mapOf("error" to msg)
    )
}