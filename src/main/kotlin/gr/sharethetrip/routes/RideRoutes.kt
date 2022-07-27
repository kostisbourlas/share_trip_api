import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.util.*

fun Route.rideRouting() {
    route("/rides") {
        get {
            return@get call.respond(RideDao.getRides())
        }

        get("/{id?}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val ride = RideDao.getRide(id) ?: return@get call.respondText(
                "Ride not found.", status = HttpStatusCode.NotFound
            )
            call.respond(ride)
        }

        post("/create") {
            val ride = call.receive<Ride>()

            if (TravelerDao.getTraveler(ride.driverId) == null) {
                return@post call.respondText("The driver does not exist.", status = HttpStatusCode.BadRequest)
            }
            if (!RideDao.createRide(
                    ride.id,
                    ride.driverId,
                    ride.departureAddress,
                    ride.arrivalAddress,
                    ride.departureDate,
                    ride.availableSeats,
                    ride.description
                )
            ) {
                return@post call.respondText("Cannot create ride.", status = HttpStatusCode.InternalServerError)
            }
            return@post call.respondText("Ride created successfully.", status = HttpStatusCode.Created)
        }

        delete("/{id?}/delete") {
            val id = call.parameters.getOrFail<Int>("id").toInt()

            if (!RideDao.deleteRide(id)) {
                call.respondText("Ride not found.", status = HttpStatusCode.NotFound)
            }
            call.respondText("Ride deleted successfully.", status = HttpStatusCode.OK)
        }

        post("/{rideId}/add-passenger/{passengerId}") {
            val rideId = call.parameters.getOrFail<Int>("rideId").toInt()
            val passengerId = call.parameters.getOrFail<Int>("passengerId").toInt()

            val ride = RideDao.getRide(rideId) ?: return@post call.respondText(
                "Ride not found.", status = HttpStatusCode.NotFound
            )
            TravelerDao.getTraveler(passengerId) ?: return@post call.respondText(
                "Passenger not found.", status = HttpStatusCode.NotFound
            )

            try {
                ride.addPassenger(passengerId)
            } catch (e: NotAvailableSeatsException) {
                return@post call.respondText(e.toString(), status = HttpStatusCode.BadRequest)
            } catch (e: InvalidPassengerException) {
                return@post call.respondText(e.toString(), status = HttpStatusCode.BadRequest)
            }
            return@post call.respondText("Passenger has been added to the ride successfully.")
        }

        post("/{rideId?}/remove-passenger/{passengerId}") {
            val rideId = call.parameters.getOrFail<Int>("rideId").toInt()
            val passengerId = call.parameters.getOrFail<Int>("passengerId").toInt()

            val ride = RideDao.getRide(rideId) ?: return@post call.respondText(
                "Ride not found.", status = HttpStatusCode.NotFound
            )
            val passenger = TravelerDao.getTraveler(passengerId) ?: return@post call.respondText(
                "Passenger not found.", status = HttpStatusCode.NotFound
            )

            try {
                ride.removePassenger(passenger)
            } catch (e: InvalidPassengerException) {
                return@post call.respondText(e.toString(), status = HttpStatusCode.BadRequest)
            }

            return@post call.respondText("Passenger has been removed from the ride successfully.")
        }
    }
}

