package org.example.todo.services

import org.example.todo.models.Todo
import org.example.todo.repositories.TodoRepository
import org.springframework.stereotype.Service
import org.example.todo.dtos.TodoDTO
import org.example.todo.models.TodoStatus
import org.example.todo.models.toDTO

@Service
class TodoService(private val repository: TodoRepository) {
    fun findAll() = repository.findAll()
    fun findById(id: String): Todo? = repository.findById(id).orElse(null)
    fun save(todo: Todo): Todo = repository.save(todo)
    fun destroy(id: String) = repository.deleteById(id)
    fun getSubTasks(todo: Todo): List<TodoDTO> = todo.subTasks.map { it.toDTO() }
    fun search(query: String): List<TodoDTO> = repository.findByTitleOrDescriptionLike(query).map { it.toDTO() }
    fun findByStatus(status: TodoStatus): List<TodoDTO> = repository.findTodosByStatus(status).map { it.toDTO() }
}
