# Task Management API

A robust REST API for managing tasks with status tracking, built with Spring Boot 3, PostgreSQL, and Docker.

## 🚀 Features

- **CRUD Operations**: Create, Read, Update, and Delete tasks
- **Status Management**: Track task status (PENDING, IN_PROGRESS, COMPLETED)
- **RESTful API**: Clean, intuitive REST endpoints
- **Database Integration**: PostgreSQL with Flyway migrations
- **Docker Support**: Easy deployment with Docker and Docker Compose
- **Comprehensive Testing**: Unit tests, integration tests, and repository tests
- **API Documentation**: Swagger/OpenAPI documentation
- **Validation**: Input validation and error handling

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.5.3
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA with Hibernate
- **Migrations**: Flyway
- **Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, Mockito, MockMvc
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven

## 📋 Prerequisites

Before running this project, make sure you have the following installed:

- **Java 17** or higher
- **Maven** 3.6+ (or use the included Maven wrapper)
- **Docker** and **Docker Compose** (for containerized deployment)
- **Git** (for cloning the repository)

## 🚀 Quick Start

### Option 1: Using Docker (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd simple-status-task
   ```

2. **Start the application with Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/api/swagger-ui.html`
   - Health Check: `http://localhost:8080/api/actuator/health`

### Option 2: Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd simple-status-task
   ```

2. **Set up PostgreSQL database**
   - Install PostgreSQL 15
   - Create a database named `taskdb`
   - Create a user `taskuser` with password `taskpass`
   - Or update `application-dev.yml` with your database credentials

3. **Run the application**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using Maven directly
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/api/swagger-ui.html`

## 🧪 Running Tests

### Run All Tests
```bash
# Using Maven wrapper
./mvnw test

# Or using Maven directly
mvn test
```

### Run Specific Test Types
```bash
# Run only unit tests
./mvnw test -Dtest=*Test

# Run only integration tests
./mvnw test -Dtest=*IntegrationTest

# Run only repository tests
./mvnw test -Dtest=*RepositoryTest
```

### Test Coverage
The project includes comprehensive tests:
- **Unit Tests**: Service layer with Mockito
- **Controller Tests**: REST endpoints with MockMvc
- **Repository Tests**: Data access layer with H2 database
- **Integration Tests**: Full application flow

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### 1. Create Task
```http
POST /tasks
Content-Type: application/json

{
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the REST API project",
  "status": "PENDING"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the REST API project",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### 2. Get Task by ID
```http
GET /tasks/{id}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the REST API project",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### 3. Update Task
```http
PUT /tasks/{id}
Content-Type: application/json

{
  "title": "Updated task title",
  "description": "Updated task description",
  "status": "IN_PROGRESS"
}
```

#### 4. Delete Task
```http
DELETE /tasks/{id}
```

**Response (204 No Content)**

### Task Status Values
- `PENDING`: Task is waiting to be started
- `IN_PROGRESS`: Task is currently being worked on
- `COMPLETED`: Task has been finished

## 🗄️ Database Schema

### Tasks Table
```sql
CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 🐳 Docker Commands

### Build and Run
```bash
# Build the application
docker build -t task-management-api .

# Run with Docker Compose
docker-compose up

# Run in background
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f app
```

### Database Access
- **PostgreSQL**: `localhost:5432`
  - Database: `taskdb`
  - Username: `taskuser`
  - Password: `taskpass`
- **pgAdmin**: `http://localhost:5050`
  - Email: `admin@task.com`
  - Password: `admin`

## 🔧 Configuration

### Environment Variables
```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/taskdb
SPRING_DATASOURCE_USERNAME=taskuser
SPRING_DATASOURCE_PASSWORD=taskpass

# Application Profile
SPRING_PROFILES_ACTIVE=prod

# Server Configuration
SERVER_PORT=8080
```

### Profiles
- **dev**: Development configuration with H2 database
- **prod**: Production configuration with PostgreSQL
- **test**: Test configuration with H2 in-memory database

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/simplestatustask/
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access layer
│   │   ├── models/              # Entity classes
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── mapper/              # Object mappers
│   │   ├── enums/               # Enumerations
│   │   ├── exception/           # Custom exceptions
│   │   └── config/              # Configuration classes
│   └── resources/
│       ├── application-*.yml    # Configuration files
│       └── db/migration/        # Flyway migrations
└── test/
    └── java/com/example/simplestatustask/
        ├── controller/          # Controller tests
        ├── service/             # Service tests
        ├── repository/          # Repository tests
        └── util/                # Test utilities
```

## 🚨 Error Handling

The API returns appropriate HTTP status codes and error messages:

- **400 Bad Request**: Invalid input data
- **404 Not Found**: Task not found
- **500 Internal Server Error**: Server-side errors

Error Response Format:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with ID: 999",
  "path": "/api/tasks/999"
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [API Documentation](http://localhost:8080/api/swagger-ui.html)
2. Review the logs: `docker-compose logs -f app`
3. Check the health endpoint: `http://localhost:8080/api/actuator/health`
4. Create an issue in the repository

## 🔄 Development Workflow

1. **Start Development Environment**
   ```bash
   docker-compose up -d
   ```

2. **Make Changes**
   - Edit source code in your IDE
   - The application will auto-reload (if using dev profile)

3. **Run Tests**
   ```bash
   ./mvnw test
   ```

4. **Build Application**
   ```bash
   ./mvnw clean package
   ```

5. **Deploy**
   ```bash
   docker-compose up --build
   ```

---

**Happy Coding! 🎉** 