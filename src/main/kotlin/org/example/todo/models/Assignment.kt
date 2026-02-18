package org.example.todo.models

import jakarta.persistence.*
import org.example.todo.dtos.AssignmentDTO
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "assignments")
class Assignment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(length = 36)
    val id: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    val todo: Todo? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Assignment
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: System.identityHashCode(this)
}

fun Assignment.toDTO(): AssignmentDTO {
    return AssignmentDTO(
        id = this.id,
        userId = this.user?.id,
        todoId = this.todo?.id
    )
}
