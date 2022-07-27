import kotlinx.serialization.Serializable

@Serializable
data class Traveler(val id: Int, val firstName: String, val lastName: String)

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
