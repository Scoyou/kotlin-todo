package org.example.todo.repositories

import org.example.todo.models.Todo
import org.example.todo.models.TodoStatus
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TodoRepository :  JpaRepository<Todo, String> {
    @Query("select t from Todo t where t.title like :query or t.description like :query order by t.id desc")
    fun findByTitleOrDescriptionLike(@Param("query") query: String): List<Todo>
    fun findTodosByStatus(@Param("status") status: TodoStatus): List<Todo>
}
