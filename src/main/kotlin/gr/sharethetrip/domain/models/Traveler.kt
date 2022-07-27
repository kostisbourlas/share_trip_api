import kotlinx.serialization.Serializable

@Serializable
data class Traveler(val id: Int, val firstName: String, val lastName: String)
