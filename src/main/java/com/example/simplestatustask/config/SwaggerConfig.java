package com.example.simplestatustask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    /**
     * Configures OpenAPI documentation
     *
     * @return OpenAPI configuration with API information
     */
    @Bean
    public OpenAPI taskManagementOpenAPI() {
        // Server configuration
        Server server = new Server()
                .url("http://localhost:8080" + contextPath)
                .description("Development server");

        // Contact information
        Contact contact = new Contact()
                .name("Task Management API Team")
                .email("aniar.kaldybaiuly@nu.alumni.edu.kz")
                .url("https://github.com/narrniar"); /*change it*/

        // License information
        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        // API information
        Info info = new Info()
                .title("Task Management API")
                .version("1.0.0")
                .description("""
                        REST API for managing tasks with full CRUD operations.
                        
                        ## Features
                        - Create, read, update, and delete tasks
                        - Task status management (PENDING, IN_PROGRESS, COMPLETED)
                        - Comprehensive error handling
                        - Input validation
                        
                        ## Task Status Values
                        - **PENDING**: Task has been created but not started
                        - **IN_PROGRESS**: Task is currently being worked on
                        - **COMPLETED**: Task has been finished
                        
                        ## Error Handling
                        All errors return a consistent error response format with:
                        - HTTP status code
                        - Error message
                        - Detailed error information (when applicable)
                        - Timestamp
                        - Request path
                        """)
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
