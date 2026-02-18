package org.example.todo.services

import org.example.todo.models.Assignment
import org.example.todo.repositories.AssignmentRepository
import org.springframework.stereotype.Service

@Service
class AssignmentService(private val repository: AssignmentRepository) {
    fun findAll() = repository.findAll()
    fun findById(id: String): Assignment? = repository.findById(id).orElse(null)
    fun save(assignment: Assignment): Assignment = repository.save(assignment)
    fun destroy(id: String) = repository.deleteById(id)
}