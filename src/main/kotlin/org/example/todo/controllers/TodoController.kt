package org.example.todo.controllers

import org.example.todo.dtos.TodoDTO
import org.example.todo.models.Todo
import org.example.todo.models.TodoStatus
import org.example.todo.models.toDTO
import org.example.todo.services.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/todos")
class TodoController(private val service: TodoService) {

    @GetMapping
    fun listTodos(): ResponseEntity<List<TodoDTO>> {
        val todos = service.findAll()
        return ResponseEntity.ok(todos.map { it.toDTO() })
    }

    @GetMapping("/{id}")
    fun findTodoById(@PathVariable id: String): ResponseEntity<TodoDTO> {
        val todo = service.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(todo.toDTO())
    }

    @GetMapping("/{id}/sub-tasks")
    fun getTodoSubTasks(@PathVariable id: String): ResponseEntity<List<TodoDTO>> {
        val todo = service.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(service.getSubTasks(todo))
    }

    @PostMapping
    fun createTodo(@RequestBody todo: Todo): ResponseEntity<TodoDTO> {
        val savedTodo = service.save(todo)
        return ResponseEntity.created(URI("/todos/${savedTodo.id}")).body(savedTodo.toDTO())
    }

    @PostMapping("/{id}/sub-tasks")
    fun createSubTask(@PathVariable id: String, @RequestBody todo: TodoUpdateRequest): ResponseEntity<TodoDTO> {
        val parentTodo = service.findById(id) ?: return ResponseEntity.notFound().build()
        val child = Todo.createSubTask(todo, parentTodo)
        val savedTodo = service.save(child)
        return ResponseEntity.created(
            URI("/todos/${parentTodo.id}/sub-tasks/${savedTodo.id}")
        ).body(
            savedTodo.toDTO()
        )
    }

    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: String,
        @RequestBody update: TodoUpdateRequest
    ): ResponseEntity<Todo> {
        val currentTodo = service.findById(id) ?: return ResponseEntity.notFound().build()

        currentTodo.title = update.title ?: currentTodo.title
        currentTodo.description = update.description ?: currentTodo.description
        currentTodo.status = update.status ?: currentTodo.status

        return ResponseEntity.ok(service.save(currentTodo))
    }

    @DeleteMapping("/{id}")
    fun deleteTodoById(@PathVariable id: String): ResponseEntity<Void> {
        service.findById(id) ?: return ResponseEntity.notFound().build()
        service.destroy(id)
        return ResponseEntity.ok().build()
    }
}

data class TodoUpdateRequest(
    val title: String,
    val description: String,
    val status: TodoStatus
)
