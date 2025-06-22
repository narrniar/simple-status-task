package com.example.simplestatustask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Main application test class
 * 
 * This test verifies that the Spring Boot application context loads successfully.
 * It's a basic smoke test to ensure all beans are properly configured.
 */
@SpringBootTest
@ActiveProfiles("test")
class SimpleStatusTaskApplicationTests {

    @Test
    void contextLoads() {
        // This test will pass if the Spring application context loads successfully
        // If there are any configuration issues, this test will fail
    }
}
