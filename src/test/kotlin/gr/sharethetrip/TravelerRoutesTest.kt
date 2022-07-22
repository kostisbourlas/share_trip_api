package gr.sharethetrip

import TravelerDao
import io.ktor.client.plugins.contentnegotiation.*
import kotlin.test.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

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
    fun testCreateTraveler() = testApplication {
        TravelerDao.createTraveler("3", "Jet", "Brains")
        val traveler = TravelerDao.getTraveler("3")

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/travelers/create") {
            contentType(ContentType.Application.Json)
            setBody(traveler)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("Traveler created successfully.", response.bodyAsText())
    }
}