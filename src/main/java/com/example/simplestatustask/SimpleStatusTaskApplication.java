package com.example.simplestatustask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SimpleStatusTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleStatusTaskApplication.class, args);

        System.out.println("""
            
            ╔══════════════════════════════════════════════════════════════╗
            ║                  Task Management API                         ║
            ║                                                              ║
            ║  🚀 Application started successfully!                       ║
            ║                                                              ║
            ║  📖 API Documentation: http://localhost:8080/api/swagger-ui.html
            ║  🔗 API Base URL: http://localhost:8080/api                 ║
            ║  📊 Health Check: http://localhost:8080/api/actuator/health ║
            ║                                                              ║
            ║  Available Endpoints:                                        ║
            ║  • POST   /api/tasks        - Create new task               ║
            ║  • GET    /api/tasks/{id}   - Get task by ID                ║
            ║  • PUT    /api/tasks/{id}   - Update task                   ║
            ║  • DELETE /api/tasks/{id}   - Delete task                   ║
            ║                                                              ║
            ╚══════════════════════════════════════════════════════════════╝
            """);
    }

}
