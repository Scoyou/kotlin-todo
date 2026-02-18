package org.example.todo.controllers

import org.example.todo.dtos.AssignmentDTO
import org.example.todo.models.Assignment
import org.example.todo.models.toDTO
import org.example.todo.services.AssignmentService
import org.example.todo.services.TodoService
import org.example.todo.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@RestController
class AssignmentController(
    private val assignmentService: AssignmentService,
    private val userService: UserService,
    private val todoService: TodoService,
) {
    @GetMapping("/{userId}/assignments")
    fun findAllForUser(@PathVariable userId: String): ResponseEntity<List<AssignmentDTO>> {
        val assignments = assignmentService.findAll()
        val userAssignments = assignments.filter { it.user?.id == userId }
        return ResponseEntity.ok(userAssignments.map { it.toDTO() })
    }

    @PostMapping("/assignments")
    fun createAssignment(@RequestBody request: CreateAssignmentRequest): ResponseEntity<AssignmentDTO> {
        val user = userService.findById(request.userId) ?: return ResponseEntity.notFound().build()
        val todo = todoService.findById(request.todoId) ?: return ResponseEntity.notFound().build()
        val assignment = Assignment(user = user, todo = todo)
        user.assignments.add(assignment)
        todo.assignments.add(assignment)
        val saved = assignmentService.save(assignment)
        return ResponseEntity.created(URI("/${saved.id}")).body(saved.toDTO())
    }
}

data class CreateAssignmentRequest(
    val userId: String,
    val todoId: String,
)
