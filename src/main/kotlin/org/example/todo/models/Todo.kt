package org.example.todo.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.example.todo.controllers.TodoUpdateRequest
import org.example.todo.dtos.TodoDTO
import org.hibernate.annotations.UuidGenerator

enum class TodoStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
}

@Entity
@Table(name = "todos")
open class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(length = 36)
    val id: String? = null,
    var title: String,
    var status: TodoStatus,
    var description: String,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Todo? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "todo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val assignments: MutableSet<Assignment> = mutableSetOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    val subTasks: MutableSet<Todo> = mutableSetOf(),
) {
    @get:Transient
    val assignees: Set<User>
        get() = assignments.mapNotNull { it.user }.toSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Todo
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: System.identityHashCode(this)

    companion object {
        fun createSubTask(request: TodoUpdateRequest, parent: Todo?): Todo {
            return Todo(
                title = request.title,
                status = request.status,
                description = request.description,
                parent = parent
            )
        }
    }
}

fun Todo.toDTO(): TodoDTO {
    return TodoDTO(
        id = this.id,
        title = this.title,
        status = this.status,
        description = this.description,
        parentId = this.parent?.id,
        userIds = this.assignments.mapNotNull { it.user?.id }.toSet()
    )
}