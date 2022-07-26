import kotlinx.serialization.Serializable

@Serializable
data class SearchRide(
    val addressFrom: String? = null,
    val addressTo: String? = null,
)

object SearchRideDao {
    fun createSearchRideObject(addressFrom: String?, addressTo: String?): SearchRide {
        return SearchRide(addressFrom, addressTo)
    }
}
