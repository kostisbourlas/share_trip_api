import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.util.*


fun Route.tripRouting() {
    route("/trips") {
        get {
            return@get call.respond(TripDao.getTrips())
        }

        get("/{id?}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val trip = TripDao.getTrip(id) ?: return@get call.respondText(
                "Trip not found.", status = HttpStatusCode.NotFound
            )
            call.respond(trip)
        }

        post("/create") {
            val trip = call.receive<Trip>()

            if (TravelerDao.getTraveler(trip.driverId) == null) {
                return@post call.respondText("The driver does not exist.", status = HttpStatusCode.BadRequest)
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
                return@post call.respondText("Cannot create trip.", status = HttpStatusCode.InternalServerError)
            }
            return@post call.respondText("Trip created successfully.", status = HttpStatusCode.Created)
        }

        delete("/{id?}/delete") {
            val id = call.parameters.getOrFail<Int>("id").toInt()

            if (!TripDao.deleteTrip(id)) {
                call.respondText("Trip not found.", status = HttpStatusCode.NotFound)
            }
            call.respondText("Trip deleted successfully.", status = HttpStatusCode.OK)
        }

        post("/{tripId}/add-passenger/{passengerId}") {
            val tripId = call.parameters.getOrFail<Int>("tripId").toInt()
            println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")

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
                return@post call.respondText(e.toString(), status = HttpStatusCode.BadRequest)
            } catch (e: InvalidPassengerException) {
                return@post call.respondText(e.toString(), status = HttpStatusCode.BadRequest)
            }
            return@post call.respondText("Traveler has been added to the trip successfully.")
        }

        post("/{tripId?}/remove-passenger/{passengerId}") {
            val tripId = call.parameters.getOrFail<Int>("tripId").toInt()
            val passengerId = call.parameters["passengerId"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val trip = TripDao.getTrip(tripId) ?: return@post call.respondText(
                "Trip not found.", status = HttpStatusCode.NotFound
            )
            val passenger = TravelerDao.getTraveler(passengerId) ?: return@post call.respondText(
                "Passenger not found.", status = HttpStatusCode.NotFound
            )

            try {
                trip.removePassenger(passenger)
            } catch (e: InvalidPassengerException) {
                return@post call.respondText(e.toString(), status = HttpStatusCode.BadRequest)
            }

            return@post call.respondText("Traveler has been removed from the trip successfully.")
        }
    }
}

