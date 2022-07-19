import kotlinx.serialization.Serializable

@Serializable
class SearchTrip(
    val addressFrom: String? = null,
    val addressTo: String? = null,
)
