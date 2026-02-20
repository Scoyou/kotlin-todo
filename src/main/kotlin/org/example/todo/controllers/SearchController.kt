package org.example.todo.controllers

import org.example.todo.dtos.TodoDTO
import org.example.todo.dtos.UserDTO
import org.example.todo.models.TodoStatus
import org.example.todo.services.TodoService
import org.example.todo.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController(
    private val todoService: TodoService,
    private val userService: UserService,
) {
    @GetMapping("/users")
    fun searchUsers(@RequestParam("q", defaultValue = "") q: String): ResponseEntity<List<UserDTO>> {
        val users = userService.search(q)
        return ResponseEntity.ok(users)
    }

    @GetMapping("/todos")
    fun searchTodos(@RequestParam("q", defaultValue = "") q: String): ResponseEntity<List<TodoDTO>> {
        val todos = todoService.search(q)
        return ResponseEntity.ok(todos)
    }

    @GetMapping("todos/{status}")
    fun searchTodosByStatus(@PathVariable status: String): ResponseEntity<List<TodoDTO>> {
        val isValidStatus = TodoStatus.entries.any() { it.name == status.uppercase() }

        if (!isValidStatus) {
            return ResponseEntity.notFound().build()
        }

        val todos = todoService.findByStatus(TodoStatus.valueOf(status.uppercase()))
        return ResponseEntity.ok(todos)
    }
}