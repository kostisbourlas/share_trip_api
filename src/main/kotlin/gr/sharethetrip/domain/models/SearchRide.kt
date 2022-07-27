import kotlinx.serialization.Serializable

@Serializable
data class SearchRide(
    val addressFrom: String? = null,
    val addressTo: String? = null,
)
