package gr.sharethetrip

import Traveler
import kotlin.test.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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
}