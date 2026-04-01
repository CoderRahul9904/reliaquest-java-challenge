package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeModel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service layer responsible for all employee business logic.
 * Uses an in-memory store to simulate a persistence layer.
 */
@Service
public class EmployeeService {

    private final List<Employee> employeeStore = new ArrayList<>();

    public EmployeeService() {
        employeeStore.add(EmployeeModel.builder()
                .uuid(UUID.randomUUID())
                .firstName("Alice")
                .lastName("Johnson")
                .fullName("Alice Johnson")
                .salary(85000)
                .age(30)
                .jobTitle("Software Engineer")
                .email("alice.johnson@company.com")
                .contractHireDate(Instant.parse("2021-03-15T00:00:00Z"))
                .contractTerminationDate(null)
                .build());

        employeeStore.add(EmployeeModel.builder()
                .uuid(UUID.randomUUID())
                .firstName("Bob")
                .lastName("Martinez")
                .fullName("Bob Martinez")
                .salary(95000)
                .age(35)
                .jobTitle("Senior Engineer")
                .email("bob.martinez@company.com")
                .contractHireDate(Instant.parse("2019-06-01T00:00:00Z"))
                .contractTerminationDate(null)
                .build());

        employeeStore.add(EmployeeModel.builder()
                .uuid(UUID.randomUUID())
                .firstName("Carol")
                .lastName("Smith")
                .fullName("Carol Smith")
                .salary(72000)
                .age(27)
                .jobTitle("QA Engineer")
                .email("carol.smith@company.com")
                .contractHireDate(Instant.parse("2022-11-10T00:00:00Z"))
                .contractTerminationDate(null)
                .build());
    }

    public List<Employee> getAllEmployees() {
        return List.copyOf(employeeStore);
    }

    public Employee getEmployeeByUuid(UUID uuid) {
        return employeeStore.stream()
                .filter(e -> e.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    public Employee createEmployee(Map<String, Object> requestBody) {
        String firstName = (String) requestBody.get("firstName");
        String lastName = (String) requestBody.get("lastName");

        if (firstName == null || lastName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "firstName and lastName are required");
        }

        EmployeeModel employee = EmployeeModel.builder()
                .uuid(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .fullName(firstName + " " + lastName)
                .salary(Optional.ofNullable((Integer) requestBody.get("salary")).orElse(0))
                .age(Optional.ofNullable((Integer) requestBody.get("age")).orElse(0))
                .jobTitle((String) requestBody.getOrDefault("jobTitle", ""))
                .email((String) requestBody.getOrDefault("email", ""))
                .contractHireDate(Instant.now())
                .contractTerminationDate(null)
                .build();

        employeeStore.add(employee);
        return employee;
    }
}