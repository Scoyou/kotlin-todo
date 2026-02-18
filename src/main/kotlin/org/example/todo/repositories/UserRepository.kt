package org.example.todo.repositories

import org.example.todo.models.User
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository :  JpaRepository<User, String>