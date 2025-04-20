package joupi.users.dto

import java.time.OffsetDateTime
import java.util.UUID
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserDTO(
    val id: UUID?,
    val firebaseUid: String,
    val email: String?,
    val name: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class CreateUserDTO(
    @field:NotBlank(message = "Firebase UID is required")
    val firebaseUid: String,
    
    @field:Email(message = "Invalid email format")
    val email: String?,
    
    val name: String?
)

data class UpdateUserDTO(
    @field:Email(message = "Invalid email format")
    val email: String?,
    
    val name: String?
)