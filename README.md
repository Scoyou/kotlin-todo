# Todo API

A Kotlin + Spring Boot REST API for managing todos, users, and user-to-todo assignments.

## Tech Stack

- Kotlin
- Spring Boot (Web MVC + Data JPA)
- PostgreSQL
- Gradle
- Java 17

## Features

- CRUD operations for todos
- Create nested todos (sub-tasks)
- Create and list users
- Assign todos to users
- Query todos by user
- Query assignments by user
- Search users and todos
- Filter todos by status

## Prerequisites

- Java 17+
- PostgreSQL running locally

## Configuration

Default configuration is in `src/main/resources/application.properties`:

- `spring.datasource.url=jdbc:postgresql://localhost:5432/todo`
- `spring.datasource.username=todouser`
- `spring.datasource.password=todoUser12345`
- `spring.jpa.hibernate.ddl-auto=update`

Create the DB/user to match this config, or update the properties for your environment.

## Run the App

```bash
./gradlew bootRun
```

App starts on:

- `http://localhost:8080`

## API Overview

### Todo endpoints

- `GET /todos` - list all todos
- `GET /todos/{id}` - get one todo
- `POST /todos` - create a todo
- `PUT /todos/{id}` - update a todo
- `DELETE /todos/{id}` - delete a todo
- `GET /todos/{id}/sub-tasks` - list sub-tasks for a todo
- `POST /todos/{id}/sub-tasks` - create a sub-task

Todo status values:

- `OPEN`
- `IN_PROGRESS`
- `DONE`

Create todo example:

```bash
curl -X POST http://localhost:8080/todos \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Ship README",
    "description": "Document the API",
    "status": "OPEN"
  }'
```

Update todo example (all fields are currently required by the request model):

```bash
curl -X PUT http://localhost:8080/todos/{todoId} \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Ship README v2",
    "description": "Updated docs",
    "status": "IN_PROGRESS"
  }'
```

### User endpoints

- `GET /users` - list users
- `GET /users/{id}` - get one user
- `POST /users` - create a user
- `DELETE /users/{id}` - delete a user
- `GET /users/{id}/todos` - list todos assigned to a user

Create user example:

```bash
curl -X POST http://localhost:8080/users \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "user1",
    "password": "12345"
  }'
```

### Assignment endpoints

- `POST /assignments` - assign a todo to a user
- `GET /{userId}/assignments` - list assignments for a user

Create assignment example:

```bash
curl -X POST http://localhost:8080/assignments \
  -H 'Content-Type: application/json' \
  -d '{
    "userId": "<user-id>",
    "todoId": "<todo-id>"
  }'
```

### Search endpoints

- `GET /search/users?q={query}` - search users by username
- `GET /search/todos?q={query}` - search todos by title or description (SQL `LIKE` pattern)
- `GET /search/todos/{status}` - list todos by status (`OPEN`, `IN_PROGRESS`, `DONE`)

Search users example:

```bash
curl "http://localhost:8080/search/users?q=user"
```

Search todos by text example:

```bash
curl "http://localhost:8080/search/todos?q=%25ship%25"
```

Search todos by status example:

```bash
curl "http://localhost:8080/search/todos/open"
```

## DTO Shapes

### TodoDTO

```json
{
  "id": "uuid",
  "title": "string",
  "status": "OPEN | IN_PROGRESS | DONE",
  "description": "string",
  "parentId": "uuid | null",
  "userIds": ["uuid"]
}
```

### UserDTO

```json
{
  "id": "uuid",
  "username": "string"
}
```

### AssignmentDTO

```json
{
  "id": "uuid",
  "userId": "uuid",
  "todoId": "uuid"
}
```

## Tests

Run tests with:

```bash
./gradlew test
```

## Notes

- IDs are generated as UUID strings.
- `password` is currently stored directly on the `User` entity (no hashing/auth flow yet).
- A sample IntelliJ HTTP client file is included at `requests.http`.
