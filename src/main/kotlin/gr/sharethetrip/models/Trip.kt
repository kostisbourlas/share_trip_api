import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Trip(
    val id: Int,
    val driverId: String,
    val departureAddress: String,
    val arrivalAddress: String,
    val departureDatetime: String,
    var availableSeats: Int,
    @Transient private val passengersIds: MutableList<String> = mutableListOf(),
    val description: String? = null,
) {
    fun addPassenger(passengerId: String) {
        if (this.driverId == passengerId) {
            throw InvalidPassengerException("You cannot be passenger as long as you are the driver of this trip.")
        }
        if (this.availableSeats <= 0) {
            throw NotAvailableSeatsException("There is no available seat in this trip.")
        }
        if (this.passengersIds.find { it == passengerId } != null) {
            throw InvalidPassengerException("Passenger has already been added to this trip.")
        }
        this.passengersIds.add(passengerId)
        this.availableSeats--
    }

    fun removePassenger(passenger: Traveler) {
        if (!this.passengersIds.removeIf { it == passenger.id }) {
            throw InvalidPassengerException("Passenger does not belong to this trip.")
        }
        this.availableSeats++
    }
}

object TripDao {
    var tripStorage: MutableList<Trip> = mutableListOf()

    init {
        tripStorage.add(
            Trip(
                id = 1,
                driverId = "1",
                departureAddress = "Athens",
                arrivalAddress = "Pilio",
                departureDatetime = "15/08/2022 12:00",
                availableSeats = 4
            )
        )
    }

    fun createTripObject(
        id: Int,
        driverId: String,
        departureAddress: String,
        arrivalAddress: String,
        departureDatetime: String,
        availableSeats: Int,
        description: String? = null
    ): Trip {
        return Trip(
            id, driverId, departureAddress, arrivalAddress, departureDatetime, availableSeats, description = description
        )
    }

    fun getTrips(): MutableList<Trip> {
        return this.tripStorage
    }

    fun getTrip(id: Int): Trip? {
        return this.tripStorage.find { it.id == id }
    }

    fun createTrip(
        id: Int,
        driverId: String,
        departureAddress: String,
        arrivalAddress: String,
        departureDatetime: String,
        availableSeats: Int,
        description: String? = null,
    ): Boolean {
        return this.tripStorage.add(
            Trip(
                id = id,
                driverId = driverId,
                departureAddress = departureAddress,
                arrivalAddress = arrivalAddress,
                departureDatetime = departureDatetime,
                availableSeats = availableSeats,
                description = description,
            )
        )
    }

    fun deleteTrip(id: Int): Boolean {
        return this.tripStorage.removeIf { it.id == id }
    }

    fun searchTrip(departureAddress: String?, arrivalAddress: String?): List<Trip> {
        return this.tripStorage.filter {
            it.departureAddress == departureAddress && it.arrivalAddress == arrivalAddress
        }
    }
}
