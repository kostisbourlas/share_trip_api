import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: String,
    val driver: Traveler,
    val departureAddress: String,
    val arrivalAddress: String,
    val departureDatetime: String,
    var availableSeats: Int,
    val passengers: MutableList<Traveler> = mutableListOf(),
) {
    fun addPassenger(passenger: Traveler) {
        if (this.availableSeats <= 0) {
            throw NotAvailableSeatsException("There is no available seat in this trip.")
        }
        if (this.passengers.find{ it.id == passenger.id} != null) {
            throw InvalidPassengerException("You have already been added to this trip.")
        }
        this.passengers.add(passenger)
        this.availableSeats--
    }
}

var tripStorage = mutableListOf<Trip>(
    Trip(
        id="1",
        driver = Traveler(id = "1", firstName = "Joe", lastName = "Doe"),
        departureAddress = "Athens",
        arrivalAddress = "Pilio",
        departureDatetime = "15/08/2022 12:00",
        availableSeats = 3
    ),
    Trip(
        id="2",
        driver = Traveler(id = "1", firstName = "Joe", lastName = "Doe"),
        departureAddress = "Athens",
        arrivalAddress = "Halkidiki",
        departureDatetime = "26/08/2022 12:00",
        availableSeats = 3
    )
)
