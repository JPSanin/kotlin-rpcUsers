import kotlinx.serialization.Serializable

@Serializable
data class User(val firstName: String, val lastName: Int, val username: String, val password: String, val ConfirmPassword: String, val birthdate: String) {

    companion object {
        const val path = "/createUser"
    }
}