package joupi.users.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import joupi.users.dto.CreateUserDTO
import joupi.users.dto.UpdateUserDTO
import joupi.users.dto.UserDTO
import joupi.users.model.User
import joupi.users.repository.UserRepository
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class UserService {

    @Inject
    lateinit var userRepository: UserRepository

    fun findAll(): List<UserDTO> {
        return userRepository.listAllActive().map { mapToDTO(it) }
    }

    fun findById(id: UUID): UserDTO {
        val user = userRepository.findById(id)
            ?: throw WebApplicationException("User not found", Response.Status.NOT_FOUND)
        return mapToDTO(user)
    }

    fun findByFirebaseUid(firebaseUid: String): UserDTO? {
        val user = userRepository.findByFirebaseUid(firebaseUid) ?: return null
        return mapToDTO(user)
    }

    @Transactional
    fun create(createUserDTO: CreateUserDTO): UserDTO {
        if (userRepository.findByFirebaseUid(createUserDTO.firebaseUid) != null) {
            throw WebApplicationException("A user with this Firebase UID already exists", Response.Status.CONFLICT)
        }

        if (createUserDTO.email != null && userRepository.findByEmail(createUserDTO.email) != null) {
            throw WebApplicationException("A user with this email already exists", Response.Status.CONFLICT)
        }

        val user = User().apply {
            id = UUID.randomUUID()
            firebaseUid = createUserDTO.firebaseUid
            email = createUserDTO.email
            name = createUserDTO.name
            createdAt = OffsetDateTime.now()
            updatedAt = OffsetDateTime.now()
        }

        userRepository.persist(user)
        return mapToDTO(user)
    }

    @Transactional
    fun update(id: UUID, updateUserDTO: UpdateUserDTO): UserDTO {
        val user = userRepository.findById(id)
            ?: throw WebApplicationException("User not found", Response.Status.NOT_FOUND)

        if (updateUserDTO.email != null && 
            updateUserDTO.email != user.email && 
            userRepository.findByEmail(updateUserDTO.email) != null) {
            throw WebApplicationException("This email is already in use", Response.Status.CONFLICT)
        }

        user.apply {
            if (updateUserDTO.email != null) email = updateUserDTO.email
            if (updateUserDTO.name != null) name = updateUserDTO.name
            updatedAt = OffsetDateTime.now()
        }

        userRepository.persist(user)
        return mapToDTO(user)
    }

    @Transactional
    fun delete(id: UUID) {
        val user = userRepository.findById(id)
            ?: throw WebApplicationException("User not found", Response.Status.NOT_FOUND)
            
        userRepository.delete(user)
    }

    private fun mapToDTO(user: User): UserDTO {
        return UserDTO(
            id = user.id,
            firebaseUid = user.firebaseUid,
            email = user.email,
            name = user.name,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
} 