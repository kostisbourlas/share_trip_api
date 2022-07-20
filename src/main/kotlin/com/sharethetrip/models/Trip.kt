import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: String,
    val driverId: String,
    val departureAddress: String,
    val arrivalAddress: String,
    val departureDatetime: String,
    var availableSeats: Int,
    val passengersIds: MutableList<String> = mutableListOf(),
    val description: String? = null,
) {
    fun addPassenger(passengerId: String) {
        if (this.driverId == passengerId) {
            throw InvalidPassengerException("You cannot be passenger as long as you are the driver of this trip.")
        }
        if (this.availableSeats <= 0) {
            throw NotAvailableSeatsException("There is no available seat in this trip.")
        }
        if (this.passengersIds.find{ it == passengerId} != null) {
            throw InvalidPassengerException("You have already been added to this trip.")
        }
        this.passengersIds.add(passengerId)
        this.availableSeats--
    }

    fun removePassenger(passenger: Traveler) {
        if (!this.passengersIds.removeIf { it == passenger.id }) {
            throw InvalidPassengerException("You do not belong to the list of passengers for this trip")
        }
        this.availableSeats++
    }
}

var tripStorage = mutableListOf<Trip>(
    Trip(
        id="1",
        driverId = "1",
        departureAddress = "Athens",
        arrivalAddress = "Pilio",
        departureDatetime = "15/08/2022 12:00",
        availableSeats = 3
    ),
    Trip(
        id="2",
        driverId = "1",
        departureAddress = "Athens",
        arrivalAddress = "Halkidiki",
        departureDatetime = "26/08/2022 12:00",
        availableSeats = 3
    )
)
