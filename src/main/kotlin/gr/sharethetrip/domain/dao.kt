package gr.sharethetrip.domain

import Ride
import SearchRide
import Traveler
import kotlinx.datetime.LocalDate

object RideDao {
    var rideStorage: MutableList<Ride> = mutableListOf()

    init {
        rideStorage.add(
            Ride(
                id = 1,
                driverId = 1,
                departureAddress = "Athens",
                arrivalAddress = "Pilio",
                departureDate = LocalDate(2022, 8, 12),
                availableSeats = 4
            )
        )
    }

    fun createRideObject(
        id: Int,
        driverId: Int,
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
        driverId: Int,
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

object TravelerDao {
    var travelerStorage: MutableList<Traveler> = mutableListOf()

    init {
        travelerStorage.add(Traveler(id = 1, firstName = "Joe", lastName = "Doe"))
        travelerStorage.add(Traveler(id = 2, firstName = "John", lastName = "Smith"))

    }

    fun createTravelerObject(id: Int, firstName: String, lastName: String): Traveler {
        return Traveler(id, firstName, lastName)
    }

    fun getTravelers(): MutableList<Traveler> {
        return this.travelerStorage
    }

    fun getTraveler(id: Int): Traveler? {
        return this.travelerStorage.find { it.id == id }
    }

    fun createTraveler(id: Int, firstName: String, lastName: String): Boolean {
        return this.travelerStorage.add(Traveler(id, firstName, lastName))
    }

    fun deleteTraveler(id: Int): Boolean {
        return this.travelerStorage.removeIf { it.id == id }
    }
}


object SearchRideDao {
    fun createSearchRideObject(addressFrom: String?, addressTo: String?): SearchRide {
        return SearchRide(addressFrom, addressTo)
    }
}
