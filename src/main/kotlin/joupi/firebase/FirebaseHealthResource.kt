package joupi.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/api/auth/health")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "Authentication health check operations")
class FirebaseHealthResource {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @GET
    @Operation(summary = "Check Firebase connectivity")
    fun checkFirebaseHealth(): Response {
        try {
            val projectId = FirebaseApp.getInstance().options.projectId
            
            return Response.ok(mapOf(
                "status" to "UP",
                "projectId" to projectId,
                "message" to "Firebase connection is working"
            )).build()
        } catch (e: Exception) {
            return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(mapOf(
                    "status" to "DOWN",
                    "error" to e.message,
                    "exception" to e.javaClass.simpleName
                ))
                .build()
        }
    }
} 