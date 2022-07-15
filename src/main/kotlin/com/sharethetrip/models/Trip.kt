import java.time.LocalDateTime


var tripStorage = mutableListOf<Trip>(
    Trip(
        id="1",
        driver = Traveler(id = "1", firstName = "Kostis", lastName = "Bourlas"),
        departureAddress = "Athens",
        arrivalAddress = "Pilio",
        departureDatetime = LocalDateTime.parse("2022-08-15T14:00:00"),
        availableSeats = 3
    ),
    Trip(
        id="2",
        driver = Traveler(id = "1", firstName = "Kostis", lastName = "Bourlas"),
        departureAddress = "Athens",
        arrivalAddress = "Halkidiki",
        departureDatetime = LocalDateTime.parse("2022-08-26T14:00:00"),
        availableSeats = 3
    )
)


data class Trip(
    val id: String,
    val driver: Traveler,
    val departureAddress: String,
    val arrivalAddress: String,
    val departureDatetime: LocalDateTime,
    var availableSeats: Int,
    val passengers: MutableList<Traveler> = mutableListOf(),
)
