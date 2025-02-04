import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    val success: Boolean,
    val accessToken: String?,
    val message: String?
)