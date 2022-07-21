import kotlinx.serialization.Serializable

@Serializable
class Traveler(val id: String, val firstName: String, val lastName: String)

object TravelerDao {
    var travelerStorage: MutableList<Traveler> = mutableListOf()

    init {
        travelerStorage.add(Traveler(id = "1", firstName = "Joe", lastName = "Doe"))
        travelerStorage.add(Traveler(id = "2", firstName = "John", lastName = "Smith"))

    }

    fun getTravelers(): MutableList<Traveler> {
        return this.travelerStorage
    }

    fun getTraveler(id: String): Traveler? {
        return this.travelerStorage.find { it.id == id }
    }

    fun createTraveler(traveler: Traveler): Boolean {
        return this.travelerStorage.add(traveler)
    }

    fun deleteTraveler(id: String): Boolean {
        return this.travelerStorage.removeIf { it.id == id }
    }
}
