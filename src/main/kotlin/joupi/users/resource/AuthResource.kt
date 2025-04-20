package joupi.users.resource

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import joupi.users.service.UserService
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "Authentication related operations")
class AuthResource {

    @Inject
    lateinit var userService: UserService

    @GET
    @Path("/check/{firebaseUid}")
    @Operation(summary = "Check if a user with the provided Firebase UID exists")
    fun checkUserExists(@PathParam("firebaseUid") firebaseUid: String): Response {
        val user = userService.findByFirebaseUid(firebaseUid)
        return if (user != null) {
            Response.ok(mapOf("exists" to true, "user" to user)).build()
        } else {
            Response.ok(mapOf("exists" to false)).build()
        }
    }
} 