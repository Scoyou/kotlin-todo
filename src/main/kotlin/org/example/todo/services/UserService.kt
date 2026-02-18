package org.example.todo.services

import org.example.todo.dtos.TodoDTO
import org.example.todo.models.User
import org.example.todo.models.toDTO
import org.example.todo.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    fun findAll() = repository.findAll()
    fun findById(id: String): User? = repository.findById(id).orElse(null)
    fun save(user: User): User = repository.save(user)
    fun destroy(id: String) = repository.deleteById(id)
    fun getUserTodos(user: User): List<TodoDTO> = user.todos.map { it.toDTO() }
}
