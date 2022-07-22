package gr.sharethetrip.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import searchTripRouting
import travelerRouting
import tripRouting

fun Application.configureRouting() {
    routing {
        travelerRouting()
        tripRouting()
        searchTripRouting()
    }
}
