package org.example.todo.repositories

import org.example.todo.models.Assignment
import org.springframework.data.jpa.repository.JpaRepository;

interface AssignmentRepository :  JpaRepository<Assignment, String>
