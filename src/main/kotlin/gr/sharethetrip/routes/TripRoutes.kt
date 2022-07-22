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
        return@get call.respond(TripDao.getTrips())
    }
}

fun Route.getTripRoute() {
    get("/trips/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Bad Request", status = HttpStatusCode.BadRequest
        )
        val trip = TripDao.getTrip(id) ?: return@get call.respondText(
            "Trip not found.", status = HttpStatusCode.NotFound
        )
        call.respond(trip)
    }
}

fun Route.createTripRoute() {
    post("/trips/create") {
        val trip = call.receive<Trip>()

        if (TravelerDao.getTraveler(trip.driverId) == null) {
            return@post call.respondText("The driver does not exist.", status = HttpStatusCode.OK)
        }
        if (!TripDao.createTrip(
                trip.id,
                trip.driverId,
                trip.departureAddress,
                trip.arrivalAddress,
                trip.departureDatetime,
                trip.availableSeats,
                trip.description
            )
        ) {
            return@post call.respondText("Cannot create trip.", status = HttpStatusCode.OK)
        }
        return@post call.respondText("Trip added successfully.", status = HttpStatusCode.Created)
    }
}

fun Route.deleteTripRoute() {
    delete("/trips/{id?}/delete") {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

        if (!TripDao.deleteTrip(id)) {
            call.respondText("Trip not Found.", status = HttpStatusCode.NotFound)
        }
        call.respondText("Trip deleted successfully.", status = HttpStatusCode.OK)
    }

}

fun Route.addPassengerToTrip() {
    post("/trips/{tripId}/add-passenger/{passengerId}") {
        val tripId = call.parameters["tripId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val passengerId = call.parameters["passengerId"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val trip = TripDao.getTrip(tripId) ?: return@post call.respondText(
            "Trip not found.", status = HttpStatusCode.NotFound
        )
        TravelerDao.getTraveler(passengerId) ?: return@post call.respondText(
            "Passenger not found.", status = HttpStatusCode.NotFound
        )

        try {
            trip.addPassenger(passengerId)
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

        val trip = TripDao.getTrip(tripId) ?: return@post call.respondText(
            "Trip not found.", status = HttpStatusCode.NotFound
        )
        val passenger = TravelerDao.getTraveler(passengerId) ?: return@post call.respondText(
            "Trip not found.", status = HttpStatusCode.NotFound
        )

        try {
            trip.removePassenger(passenger)
        } catch (e: InvalidPassengerException) {
            return@post call.respondText(e.toString(), status = HttpStatusCode.OK)
        }

        return@post call.respondText("Traveler has been removed from the trip successfully.")
    }
}
