package org.example.todo.repositories

import org.example.todo.models.Todo
import org.springframework.data.jpa.repository.JpaRepository;

interface TodoRepository :  JpaRepository<Todo, String>
