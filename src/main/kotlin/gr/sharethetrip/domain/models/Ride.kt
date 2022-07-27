import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Ride(
    val id: Int,
    val driverId: Int,
    val departureAddress: String,
    val arrivalAddress: String,
    val departureDate: LocalDate,
    var availableSeats: Int,
    @Transient private val passengersIds: MutableList<Int> = mutableListOf(),
    val description: String? = null,
) {
    fun addPassenger(passengerId: Int) {
        if (this.driverId == passengerId) {
            throw InvalidPassengerException("You cannot be passenger as long as you are the driver of this ride.")
        }
        if (this.availableSeats <= 0) {
            throw NotAvailableSeatsException("There is no available seat in this ride.")
        }
        if (this.passengersIds.find { it == passengerId } != null) {
            throw InvalidPassengerException("Passenger has already been added to this ride.")
        }
        this.passengersIds.add(passengerId)
        this.availableSeats--
    }

    fun removePassenger(passenger: Traveler) {
        if (!this.passengersIds.removeIf { it == passenger.id }) {
            throw InvalidPassengerException("Passenger does not belong to this ride.")
        }
        this.availableSeats++
    }
}
