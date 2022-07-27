import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.searchRideRouting() {
    searchRideRoute()
}

fun Route.searchRideRoute() {
    get("/search/rides") {
        val searchRide = call.receive<SearchRide>()
        val rides = RideDao.searchRide(searchRide.addressFrom, searchRide.addressTo)
        return@get call.respond(rides)
    }
}
