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
        val driverId = "Invalid"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/create") {
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
        val tripId = "100"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // Create a new Trip in storage before deleting it.
        client.post("/trips/create") {
            contentType(ContentType.Application.Json)
            setBody(
                TripDao.createTripObject(
                    tripId, "1", "Athens", "Pilio", "18/12/2022", 3
                )
            )
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

    @Test
    fun testAddPassengerToTrip() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Traveler has been added to the trip successfully.", response.bodyAsText())
    }

    @Test
    fun testAddDriverToTripAsPassenger() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/1/add-passenger/1") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(
            response.bodyAsText(), "You cannot be passenger as long as you are the driver of this trip."
        )
    }

    @Test
    fun testAddPassengerNotAvailableSeats() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // Create trip with no available seats
        client.post("/trips/create") {
            contentType(ContentType.Application.Json)
            setBody(
                TripDao.createTripObject(
                    "100", "1", "Athens", "Pilio", "18/12/2022", 0
                )
            )
        }
        val response = client.post("/trips/100/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "There is no available seat in this trip.")
    }

    @Test
    fun testAddPassengerToTripTwice() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/trips/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        val response = client.post("/trips/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "Passenger has already been added to this trip.")
    }

    @Test
    fun testAddPassengerWithInvalidTripId() = testApplication {
        val invalidTripId = "invalidTripId"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/$invalidTripId/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Trip not found.", response.bodyAsText())
    }

    @Test
    fun testAddPassengerWithInvalidPassengerId() = testApplication {
        val invalidPassengerId = "invalidPassengerId"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/1/add-passenger/$invalidPassengerId") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Passenger not found.", response.bodyAsText())
    }

    @Test
    fun testRemovePassengerFromTrip() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        // Add a passenger to the trip before removing them
        client.post("/trips/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        val response = client.post("/trips/1/remove-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Traveler has been removed from the trip successfully.", response.bodyAsText())
    }

    @Test
    fun testRemovePassengerThatDoesNotBelongToTrip() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/1/remove-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "Passenger does not belong to this trip.")
    }

    @Test
    fun testRemovePassengerInvalidTripId() = testApplication {
        val invalidTripId = "invalidPassengerId"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/$invalidTripId/remove-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Trip not found.", response.bodyAsText())
    }

    @Test
    fun testRemovePassengerInvalidPassengerId() = testApplication {
        val invalidPassengerId = "invalidPassengerId"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/trips/1/remove-passenger/$invalidPassengerId") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Passenger not found.", response.bodyAsText())
    }
}