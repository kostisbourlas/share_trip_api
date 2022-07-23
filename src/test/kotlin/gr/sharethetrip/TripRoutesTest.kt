package gr.sharethetrip

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*


class TripRoutesTest {
    @Test
    fun testGetAllTrips() = testApplication {
        val response = client.get("/trips")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetTripById() = testApplication {
        val response = client.get("/trips/1")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetTripWithInvalidId() = testApplication {
        val invalidId: String = "5000"
        val response = client.get("/trips/$invalidId")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Trip not found.", response.bodyAsText())
    }

    @Test
    fun testGetTripMissingId() = testApplication {
        val response = client.get("/trips/")

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("Missing Id.", response.bodyAsText())
    }

    @Test
    fun testCreateTrip() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/trips/create") {
            contentType(ContentType.Application.Json)
            setBody(
                TripDao.createTripObject(
                    "100", "1", "Athens", "Pilio", "18/12/2022", 3
                )
            )
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("Trip created successfully.", response.bodyAsText())
    }

    @Test
    fun testCreateTripInvalidDriverId() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/trips/create") {
            val driverId = "Invalid"
            contentType(ContentType.Application.Json)
            setBody(
                TripDao.createTripObject(
                    "100", driverId, "Athens", "Pilio", "18/12/2022", 3
                )
            )
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("The driver does not exist.", response.bodyAsText())
    }

    @Test
    fun testCreateTripUnableToSave() = testApplication {
        // Not implemented yet
    }

    @Test
    fun testDeleteTrip() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val tripId = "100"

        // Create a new Trip in storage before deleting it.
        client.post("/trips/create") {
            contentType(ContentType.Application.Json)
            setBody(TripDao.createTripObject(
                tripId, "1", "Athens", "Pilio", "18/12/2022", 3
            ))
        }

        val response = client.delete("/trips/$tripId/delete") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Trip deleted successfully.", response.bodyAsText())
    }

    @Test
    fun testDeleteTripWIthInvalidId() = testApplication {
        val invalidId: String = "5000"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.delete("/trips/$invalidId/delete") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Trip not found.", response.bodyAsText())
    }

}