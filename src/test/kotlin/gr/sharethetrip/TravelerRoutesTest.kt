package gr.sharethetrip

import TravelerDao
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals


class TravelerRoutesTest {
    @Test
    fun testGetTravelers() = testApplication {
        val response: HttpResponse = client.get("/travelers")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetTraveler() = testApplication {
        val response: HttpResponse = client.get("/travelers/1")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetTravelerWithNoId() = testApplication {
        val response: HttpResponse = client.get("/travelers/")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testGetTravelerWithNInvalidId() = testApplication {
        val invalidId: String = "5000"
        val response: HttpResponse = client.get("/travelers/$invalidId")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("No traveler with id $invalidId.", response.bodyAsText())
    }

    @Test
    fun testCreateTraveler() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/travelers/create") {
            contentType(ContentType.Application.Json)
            setBody(TravelerDao.createTravelerObject(100, "Jet", "Brains"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("Traveler created successfully.", response.bodyAsText())
    }

    @Test
    fun testDeleteTraveler() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        // Create a new Traveler in storage before deleting it.
        client.post("/travelers/create") {
            contentType(ContentType.Application.Json)
            setBody(TravelerDao.createTravelerObject(100, "Jet", "Brains"))
        }

        val response = client.delete("/travelers/100/delete") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Traveler deleted successfully.", response.bodyAsText())
    }

    @Test
    fun testDeleteTravelerWIthInvalidId() = testApplication {
        val invalidId: String = "5000"
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.delete("/travelers/$invalidId/delete") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Traveler not Found.", response.bodyAsText())
    }
}