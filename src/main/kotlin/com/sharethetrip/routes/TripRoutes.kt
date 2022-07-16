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
    removePassengerFromTrip()
}

fun Route.listTripRoute() {
    get("/trips") {
        if (tripStorage.isNotEmpty()) {
            return@get call.respond(tripStorage)
        }
    }
}

fun Route.getTripRoute() {
    get("/trips/{id}") {
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
    post("/trips/create"){
        val trip = call.receive<Trip>()
        tripStorage.add(trip)
        call.respondText("Trip added successfully.", status = HttpStatusCode.Created)
    }
}

fun Route.deleteTripRoute() {
    delete("/trips/{id?}/delete") {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

        if (tripStorage.removeIf{ it.id == id }) {
            call.respondText("Trip deleted successfully.", status = HttpStatusCode.Accepted)
        } else {
            call.respondText("Trip not Found", status = HttpStatusCode.NotFound)
        }
    }

}

fun Route.addPassengerToTrip() {
    post("/trips/{tripId}/add-passenger/{passengerId}") {
        val tripId = call.parameters["tripId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val passengerId = call.parameters["passengerId"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val trip = tripStorage.find { it.id == tripId }
            ?: return@post call.respondText("Trip not found.", status = HttpStatusCode.NotFound)
        val passenger = travelerStorage.find { it.id == passengerId }
            ?: return@post call.respondText("Trip not found.", status = HttpStatusCode.NotFound)

        try {
            trip.addPassenger(passenger)
        } catch (e: NotAvailableSeatsException) {
            return@post call.respondText(e.toString(), status = HttpStatusCode.OK)
        } catch (e: InvalidPassengerException) {
            return@post call.respondText(e.toString(), status = HttpStatusCode.OK)
        }

        return@post call.respondText("Traveler has been added to the trip successfully.")
    }
}

fun Route.removePassengerFromTrip() {
    post("/trips/{tripId}/remove-passenger/{passengerId}") {
        val tripId = call.parameters["tripId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val passengerId = call.parameters["passengerId"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val trip = tripStorage.find { it.id == tripId }
            ?: return@post call.respondText("Trip not found.", status = HttpStatusCode.NotFound)
        val passenger = travelerStorage.find { it.id == passengerId }
            ?: return@post call.respondText("Trip not found.", status = HttpStatusCode.NotFound)

        try {
            trip.removePassenger(passenger)
        } catch (e: InvalidPassengerException) {
            return@post call.respondText(e.toString(), status = HttpStatusCode.OK)
        }

        return@post call.respondText("Traveler has been removed from the trip successfully.")
    }
}
