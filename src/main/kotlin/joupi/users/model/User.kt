package joupi.users.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import java.time.OffsetDateTime
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User : PanacheEntityBase {
    @Id
    var id: UUID? = null

    @Column(name = "firebase_uid", nullable = false, unique = true)
    lateinit var firebaseUid: String

    @Column(unique = true)
    var email: String? = null

    var name: String? = null

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
} 