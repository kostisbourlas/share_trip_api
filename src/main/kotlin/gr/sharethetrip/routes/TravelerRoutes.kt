import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.travelerRouting() {
    route("/travelers") {
        get {
            return@get call.respond(TravelerDao.getTravelers())
        }

        get("/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id.", status = HttpStatusCode.BadRequest
            )
            val traveler = TravelerDao.getTraveler(id) ?: return@get call.respondText(
                "No traveler with id ${id}.", status = HttpStatusCode.NotFound
            )

            call.respond(traveler)
        }

        post("/create") {
            val traveler = call.receive<Traveler>()
            TravelerDao.createTraveler(traveler.id, traveler.firstName, traveler.lastName)
            call.respondText("Traveler created successfully.", status = HttpStatusCode.Created)
        }

        delete("/{id?}/delete") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (TravelerDao.deleteTraveler(id)) {
                call.respondText("Traveler deleted successfully.", status = HttpStatusCode.OK)
            } else {
                call.respondText("Traveler not Found.", status = HttpStatusCode.NotFound)
            }
        }
    }
}
