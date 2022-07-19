import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.searchTripRouting() {
    searchTripRoute()
}

fun Route.searchTripRoute() {
    get("/search/trips") {
        val searchTrip = call.receive<SearchTrip>()
        val trips = tripStorage.filter {
            it.departureAddress == searchTrip.addressFrom &&
            it.arrivalAddress == searchTrip.addressTo
        }
        return@get call.respond(trips)
    }
}
