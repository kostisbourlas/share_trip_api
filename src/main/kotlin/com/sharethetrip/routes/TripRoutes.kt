import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*


fun Route.tripRouting() {
    listTripRoute()
    getTripRoute()
    createTripRoute()
    deleteTripRoute()
}

fun Route.listTripRoute() {
    get("/trip") {
        if (tripStorage.isNotEmpty()) {
            call.respond(tripStorage)
        }
    }
}

fun Route.getTripRoute() {
    get("/trip/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Bad Request", status = HttpStatusCode.BadRequest
        )
        val trip = tripStorage.find { it.id == id } ?: return@get call.respondText(
            "Trip not found.", status = HttpStatusCode.NotFound
        )
        call.respond(trip)
    }
}

fun Route.createTripRoute() {
    post(){
        val trip = call.receive<Trip>()
        tripStorage.add(trip)
        call.respondText("Trip added successfully.", status = HttpStatusCode.Created)
    }
}

fun Route.deleteTripRoute() {
    delete("{id?}") {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

        if (tripStorage.removeIf{ it.id == id }) {
            call.respondText("Trip deleted successfully.", status = HttpStatusCode.Accepted)
        } else {
            call.respondText("Trip not Found", status = HttpStatusCode.NotFound)
        }
    }

}