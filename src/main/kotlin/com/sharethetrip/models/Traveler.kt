import kotlinx.serialization.Serializable

@Serializable
data class Traveler(val id: String, val firstName: String, val lastName: String)

val travelerStorage = mutableListOf<Traveler>(
    Traveler(
        id = "1",
        firstName = "Joe",
        lastName = "Doe"
    ),
    Traveler(
        id = "2",
        firstName = "John",
        lastName = "Smith"
    ),
)
