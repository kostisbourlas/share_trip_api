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
        val trips = TripDao.searchTrip(searchTrip.addressFrom, searchTrip.addressTo)
        return@get call.respond(trips)
    }
}
