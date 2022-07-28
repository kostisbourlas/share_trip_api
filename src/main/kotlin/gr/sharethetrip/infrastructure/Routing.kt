package gr.sharethetrip.infrastructure

import io.ktor.server.application.*
import io.ktor.server.routing.*
import rideRouting
import searchRideRouting
import travelerRouting


fun Application.configureRouting() {
    routing {
        travelerRouting()
        rideRouting()
        searchRideRouting()
    }
}
