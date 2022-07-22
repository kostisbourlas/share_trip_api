import kotlinx.serialization.Serializable

@Serializable
data class SearchTrip(
    val addressFrom: String? = null,
    val addressTo: String? = null,
)
