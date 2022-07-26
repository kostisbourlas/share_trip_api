import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Ride(
    val id: Int,
    val driverId: String,
    val departureAddress: String,
    val arrivalAddress: String,
    val departureDate: LocalDate,
    var availableSeats: Int,
    @Transient private val passengersIds: MutableList<String> = mutableListOf(),
    val description: String? = null,
) {
    fun addPassenger(passengerId: String) {
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

object RideDao {
    var rideStorage: MutableList<Ride> = mutableListOf()

    init {
        rideStorage.add(
            Ride(
                id = 1,
                driverId = "1",
                departureAddress = "Athens",
                arrivalAddress = "Pilio",
                departureDate = LocalDate(2022, 8, 12),
                availableSeats = 4
            )
        )
    }

    fun createRideObject(
        id: Int,
        driverId: String,
        departureAddress: String,
        arrivalAddress: String,
        departureDate: LocalDate,
        availableSeats: Int,
        description: String? = null
    ): Ride {
        return Ride(
            id, driverId, departureAddress, arrivalAddress, departureDate, availableSeats, description = description
        )
    }

    fun getRides(): MutableList<Ride> {
        return this.rideStorage
    }

    fun getRide(id: Int): Ride? {
        return this.rideStorage.find { it.id == id }
    }

    fun createRide(
        id: Int,
        driverId: String,
        departureAddress: String,
        arrivalAddress: String,
        departureDate: LocalDate,
        availableSeats: Int,
        description: String? = null,
    ): Boolean {
        return this.rideStorage.add(
            Ride(
                id = id,
                driverId = driverId,
                departureAddress = departureAddress,
                arrivalAddress = arrivalAddress,
                departureDate = departureDate,
                availableSeats = availableSeats,
                description = description,
            )
        )
    }

    fun deleteRide(id: Int): Boolean {
        return this.rideStorage.removeIf { it.id == id }
    }

    fun searchRide(departureAddress: String?, arrivalAddress: String?): List<Ride> {
        return this.rideStorage.filter {
            it.departureAddress == departureAddress && it.arrivalAddress == arrivalAddress
        }
    }
}
