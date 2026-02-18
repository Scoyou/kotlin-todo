package org.example.todo.dtos

import org.example.todo.models.TodoStatus

data class TodoDTO(
    val id: String?,
    val title: String,
    val status: TodoStatus,
    val description: String,
    val parentId: String?,
    val userIds: Set<String>,
)
