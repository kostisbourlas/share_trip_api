import kotlinx.serialization.Serializable


var tripStorage = mutableListOf<Trip>(
    Trip(
        id="1",
        driver = Traveler(id = "1", firstName = "Kostis", lastName = "Bourlas"),
        departureAddress = "Athens",
        arrivalAddress = "Pilio",
        departureDatetime = "15/08/2022 12:00",
        availableSeats = 3
    ),
    Trip(
        id="2",
        driver = Traveler(id = "1", firstName = "Kostis", lastName = "Bourlas"),
        departureAddress = "Athens",
        arrivalAddress = "Halkidiki",
        departureDatetime = "26/08/2022 12:00",
        availableSeats = 3
    )
)

@Serializable
data class Trip(
    val id: String,
    val driver: Traveler,
    val departureAddress: String,
    val arrivalAddress: String,
    val departureDatetime: String,
    var availableSeats: Int,
    val passengers: MutableList<Traveler> = mutableListOf(),
)
