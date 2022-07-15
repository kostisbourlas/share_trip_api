import kotlinx.serialization.Serializable

@Serializable
data class Traveler(val id: String, val firstName: String, val lastName: String)

val travelerStorage = mutableListOf<Traveler>()
