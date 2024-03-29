package gr.sharethetrip.routes

import gr.sharethetrip.domain.SearchRideDao
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals


class SearchRideRoutesTest {
    @Test
    fun searchRidesByDepartureAndArrivalAddresses() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("search/rides") {
            contentType(ContentType.Application.Json)
            setBody(SearchRideDao.createSearchRideObject("Athens", "Pilio"))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            """
                [{"id":1,"driverId":1,"departureAddress":"Athens","arrivalAddress":"Pilio","departureDate":"2022-08-12","availableSeats":4,"description":null}]
            """.trimIndent(), response.bodyAsText()
        )
    }
}