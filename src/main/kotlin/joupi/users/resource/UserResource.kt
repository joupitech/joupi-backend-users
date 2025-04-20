package joupi.users.resource

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import joupi.users.dto.CreateUserDTO
import joupi.users.dto.UpdateUserDTO
import joupi.users.service.UserService
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.net.URI
import java.util.UUID

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "User management operations")
class UserResource {

    @Inject
    lateinit var userService: UserService

    @GET
    @Operation(summary = "List all users")
    fun getAll(): Response {
        return Response.ok(userService.findAll()).build()
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Find user by ID")
    fun getById(@PathParam("id") id: UUID): Response {
        return Response.ok(userService.findById(id)).build()
    }

    @GET
    @Path("/firebase/{firebaseUid}")
    @Operation(summary = "Find user by Firebase UID")
    fun getByFirebaseUid(@PathParam("firebaseUid") firebaseUid: String): Response {
        val user = userService.findByFirebaseUid(firebaseUid)
        return if (user != null) {
            Response.ok(user).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @POST
    @Operation(summary = "Create a new user")
    fun create(@Valid createUserDTO: CreateUserDTO): Response {
        val newUser = userService.create(createUserDTO)
        return Response
            .created(URI.create("/api/users/${newUser.id}"))
            .entity(newUser)
            .build()
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing user")
    fun update(
        @PathParam("id") id: UUID,
        @Valid updateUserDTO: UpdateUserDTO
    ): Response {
        return Response.ok(userService.update(id, updateUserDTO)).build()
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a user")
    fun delete(@PathParam("id") id: UUID): Response {
        userService.delete(id)
        return Response.noContent().build()
    }
} 