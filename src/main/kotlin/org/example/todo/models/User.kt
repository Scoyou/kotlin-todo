package org.example.todo.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.example.todo.dtos.UserDTO
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(length = 36)
    val id: String? = null,
    val username: String,
    val password: String,
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val assignments: MutableSet<Assignment> = mutableSetOf(),
) {
    @get:Transient
    val todos: Set<Todo>
        get() = assignments.mapNotNull { it.todo }.toSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as User
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: System.identityHashCode(this)
}

fun User.toDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        username = this.username,
    )
}
