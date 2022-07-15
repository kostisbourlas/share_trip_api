import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.travelerRouting() {
    route("/traveler") {
        get {
            if (travelerStorage.isNotEmpty()) {
                call.respond(travelerStorage)
            } else {
                call.respondText("No travelers found.", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val traveler = travelerStorage.find{ it.id == id } ?: return@get call.respondText(
                "No traveler with id ${id}.",
                status = HttpStatusCode.NotFound
            )

            call.respond(traveler)
        }
        post {
            val traveler = call.receive<Traveler>()
            travelerStorage.add(traveler)
            call.respondText("Traveler added successfully.", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (travelerStorage.removeIf{ it.id == id }) {
                call.respondText("Traveler deleted successfully.", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Traveler not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
