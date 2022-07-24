import kotlinx.serialization.Serializable

@Serializable
data class SearchTrip(
    val addressFrom: String? = null,
    val addressTo: String? = null,
)

object SearchTripDao {
    fun createSearchTripObject(addressFrom: String?, addressTo: String?): SearchTrip {
        return SearchTrip(addressFrom, addressTo)
    }
}
