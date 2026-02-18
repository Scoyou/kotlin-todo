package org.example.todo.controllers

import org.example.todo.dtos.TodoDTO
import org.example.todo.dtos.UserDTO
import org.example.todo.models.User
import org.example.todo.models.toDTO
import org.example.todo.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/users")
class UserController(private val service: UserService) {
    @GetMapping
    fun getUsers(): ResponseEntity<List<UserDTO>> {
        val users = service.findAll()
        val userDTOs = users.map { it.toDTO() }.toMutableList()
        return ResponseEntity.ok(userDTOs)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<UserDTO> {
        val user = service.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user.toDTO())
    }

    @GetMapping("/{id}/todos")
    fun getUserTodos(@PathVariable id: String): ResponseEntity<List<TodoDTO>> {
        val user = service.findById(id) ?: return ResponseEntity.notFound().build()
        val todos = service.getUserTodos(user)
        return ResponseEntity.ok(todos)
    }

    @PostMapping()
    fun addUser(@RequestBody user: User): ResponseEntity<UserDTO> {
        service.save(user)
        return ResponseEntity.created(URI("/${user.id}")).body(user.toDTO())
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        service.findById(id) ?: return ResponseEntity.notFound().build()
        service.destroy(id)
        return ResponseEntity.noContent().build()
    }
}
