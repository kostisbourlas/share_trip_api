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
    addPassengerToTrip()
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
    post("/trip"){
        val trip = call.receive<Trip>()
        tripStorage.add(trip)
        call.respondText("Trip added successfully.", status = HttpStatusCode.Created)
    }
}

fun Route.deleteTripRoute() {
    delete("/trip/{id?}") {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

        if (tripStorage.removeIf{ it.id == id }) {
            call.respondText("Trip deleted successfully.", status = HttpStatusCode.Accepted)
        } else {
            call.respondText("Trip not Found", status = HttpStatusCode.NotFound)
        }
    }

}

fun Route.addPassengerToTrip() {
    post("/trip/{id}/add-passenger") {
        val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val passenger = call.receive<Traveler>()
        val trip = tripStorage.find { it.id == id }
            ?: return@post call.respondText("Trip not found.", status = HttpStatusCode.NotFound)

        try {
            trip.addPassenger(passenger)
            return@post call.respondText("Traveler has been added to the trip successfully.")
        }
        catch (e: NotAvailableSeatsException) {
            return@post call.respondText(e.toString(), status = HttpStatusCode.OK)
        }
        catch (e: InvalidPassengerException) {
            return@post call.respondText(e.toString(), status = HttpStatusCode.OK)
        }
    }
}
