import kotlinx.serialization.Serializable

@Serializable
data class AddAmountRs (
    val id: Int,
    val value: Long
)