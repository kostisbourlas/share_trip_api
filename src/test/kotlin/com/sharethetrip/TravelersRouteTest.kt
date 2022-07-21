import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.test.*
import io.ktor.server.routing.*


class CustomerTests {
    @Test
    fun testGetAllTravelers() = testApplication {
        application {
            configureRouting()
        }
        install(ContentNegotiation) {
            json()
        }
        val response: HttpResponse = client.get("/travelers")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}