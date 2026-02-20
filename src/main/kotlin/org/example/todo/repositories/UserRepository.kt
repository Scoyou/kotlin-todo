package org.example.todo.repositories

import org.example.todo.models.User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository :  JpaRepository<User, String> {
    @Query("select u from User u where u.username like :username")
    fun findByUsernameLike(@Param("username") username: String): List<User>
}