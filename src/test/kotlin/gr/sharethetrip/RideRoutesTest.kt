package gr.sharethetrip

import RideDao
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals


class RideRoutesTest {
    @Test
    fun testGetAllRides() = testApplication {
        val response = client.get("/rides")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetRideById() = testApplication {
        val response = client.get("/rides/1")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetRideWithInvalidId() = testApplication {
        val invalidId: String = "5000"
        val response = client.get("/rides/$invalidId")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Ride not found.", response.bodyAsText())
    }

    @Test
    fun testGetRideMissingId() = testApplication {
        val response = client.get("/rides/")

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testCreateRide() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/create") {
            contentType(ContentType.Application.Json)
            setBody(
                RideDao.createRideObject(
                    100, 1, "Athens", "Pilio", LocalDate(2022, 8, 12), 3
                )
            )
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("Ride created successfully.", response.bodyAsText())
    }

    @Test
    fun testCreateRideInvalidDriverId() = testApplication {
        val driverId = 5000
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/create") {
            contentType(ContentType.Application.Json)
            setBody(
                RideDao.createRideObject(
                    100, driverId, "Athens", "Pilio", LocalDate(2022, 8, 12), 3
                )
            )
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("The driver does not exist.", response.bodyAsText())
    }

    @Test
    fun testCreateRideUnableToSave() = testApplication {
        // Not implemented yet
    }

    @Test
    fun testDeleteRide() = testApplication {
        val rideId = 100
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // Create a new Ride in storage before deleting it.
        client.post("/rides/create") {
            contentType(ContentType.Application.Json)
            setBody(
                RideDao.createRideObject(
                    rideId, 1, "Athens", "Pilio", LocalDate(2022, 8, 12), 3
                )
            )
        }

        val response = client.delete("/rides/$rideId/delete") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Ride deleted successfully.", response.bodyAsText())
    }

    @Test
    fun testDeleteRideWIthInvalidId() = testApplication {
        val invalidId: String = "5000"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.delete("/rides/$invalidId/delete") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Ride not found.", response.bodyAsText())
    }

    @Test
    fun testAddPassengerToRide() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Passenger has been added to the ride successfully.", response.bodyAsText())
    }

    @Test
    fun testAddDriverToRideAsPassenger() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/1/add-passenger/1") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(
            response.bodyAsText(), "You cannot be passenger as long as you are the driver of this ride."
        )
    }

    @Test
    fun testAddPassengerNotAvailableSeats() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // Create Ride with no available seats
        client.post("/rides/create") {
            contentType(ContentType.Application.Json)
            setBody(
                RideDao.createRideObject(
                    100, 1, "Athens", "Pilio", LocalDate(2022, 8, 12), 0
                )
            )
        }
        val response = client.post("/rides/100/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "There is no available seat in this ride.")
    }

    @Test
    fun testAddPassengerToRideTwice() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/rides/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        val response = client.post("/rides/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "Passenger has already been added to this ride.")
    }

    @Test
    fun testAddPassengerWithInvalidRideId() = testApplication {
        val invalidRideId = 50000
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/$invalidRideId/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Ride not found.", response.bodyAsText())
    }

    @Test
    fun testAddPassengerWithInvalidPassengerId() = testApplication {
        val invalidPassengerId = 5000
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/1/add-passenger/$invalidPassengerId") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Passenger not found.", response.bodyAsText())
    }

    @Test
    fun testRemovePassengerFromRide() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        // Add a passenger to the ride before removing them
        client.post("/rides/1/add-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        val response = client.post("/rides/1/remove-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Passenger has been removed from the ride successfully.", response.bodyAsText())
    }

    @Test
    fun testRemovePassengerThatDoesNotBelongToRide() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/1/remove-passenger/2") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertContains(response.bodyAsText(), "Passenger does not belong to this ride.")
    }

    @Test
    fun testRemovePassengerInvalidRideId() = testApplication {
        val invalidRideId = 50000
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/$invalidRideId/remove-passenger/2") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Ride not found.", response.bodyAsText())
    }

    @Test
    fun testRemovePassengerInvalidPassengerId() = testApplication {
        val invalidPassengerId = 5000
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/rides/1/remove-passenger/$invalidPassengerId") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Passenger not found.", response.bodyAsText())
    }
}