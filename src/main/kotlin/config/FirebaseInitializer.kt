package config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.util.Optional

@ApplicationScoped
class FirebaseInitializer {

    @ConfigProperty(name = "FIREBASE_CREDENTIALS_JSON")
    lateinit var firebaseCredentialsJson: Optional<String>

    @ConfigProperty(name = "joupi.firebase.service-account-key.path")
    lateinit var serviceAccountPath: Optional<String>

    private val log: Logger = Logger.getLogger(FirebaseInitializer::class.java)

    fun onStart(@Observes ev: StartupEvent?) {
        if (FirebaseApp.getApps().isNotEmpty()) {
            log.info("Firebase Admin SDK already initialized.")
            return
        }

        try {
            val credentialsStream: InputStream?
            var initSource = ""

            if (firebaseCredentialsJson.isPresent && firebaseCredentialsJson.get().isNotBlank()) {
                credentialsStream = ByteArrayInputStream(firebaseCredentialsJson.get().toByteArray(Charsets.UTF_8))
                initSource = "environment variable FIREBASE_CREDENTIALS_JSON"
            } else if (serviceAccountPath.get().isNotBlank()) {
                credentialsStream = FileInputStream(serviceAccountPath.get())
                initSource = "classpath resource $serviceAccountPath"
            } else {
                credentialsStream = null
            }

            if (credentialsStream != null) {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                FirebaseApp.initializeApp(options)
                log.info("Firebase Admin SDK initialized successfully from $initSource.")
                credentialsStream.close()
            } else {
                log.warn("Could not find Firebase credentials via environment variable or classpath. Firebase Admin SDK not initialized.")
            }

        } catch (e: Exception) {
            log.error("Error initializing Firebase Admin SDK", e)
        }
    }
}