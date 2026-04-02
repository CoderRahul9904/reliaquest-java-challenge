# ReliaQuest Entry-Level Java Challenge

## Solution Overview

This implementation exposes employee data as a **protected REST API** using Spring Boot,
designed to integrate with the Employees-R-US SaaS platform via webhooks.

### Architecture

A standard three-layer Spring Boot architecture was followed:

```
EmployeeController  (HTTP routing, request/response mapping)
       ↓
EmployeeService     (business logic, in-memory data store)
       ↓
EmployeeModel       (concrete implementation of Employee interface)
```

**Key design decisions:**
- `EmployeeModel` implements the provided `Employee` interface as a binding contract
- `EmployeeService` uses an in-memory `List<Employee>` to simulate a persistence layer, pre-seeded with 3 mock employees
- `ApiSecurityFilter` enforces `X-API-Key` header authentication on all requests
- The API key is read from the `API_KEY` environment variable (falls back to a default for local development)
- `List.copyOf()` is used in `getAllEmployees()` for a defensive copy of the store
- `createEmployee` returns HTTP 201 Created on success, and 400 Bad Request if required fields are missing

---

## Running the API

**Prerequisites:** Java 17+

```bash
./gradlew bootRun
```

The server starts on `http://localhost:8080`.

To use a custom API key:
```bash
API_KEY=my-secret ./gradlew bootRun
```

---

## API Reference

All endpoints require the `X-API-Key` header. Requests without a valid key return `401 Unauthorized`.

### Authentication

| Header | Value |
|--------|-------|
| `X-API-Key` | `rq-secret-key-2024` (default) or value of `API_KEY` env var |

---

### GET /api/v1/employee — Get All Employees

Returns the full list of employees, unfiltered.

**Request:**
```
GET /api/v1/employee
X-API-Key: rq-secret-key-2024
```

**Response — 200 OK:**
```json
[
  {
    "uuid": "5e8be5bf-b3c3-4d2e-81f6-1cf14ed7cbfa",
    "firstName": "Alice",
    "lastName": "Johnson",
    "fullName": "Alice Johnson",
    "salary": 85000,
    "age": 30,
    "jobTitle": "Software Engineer",
    "email": "alice.johnson@company.com",
    "contractHireDate": "2021-03-15T00:00:00Z",
    "contractTerminationDate": null
  },
  {
    "uuid": "c065ca11-7136-4cf7-b3c9-31c546213295",
    "firstName": "Bob",
    "lastName": "Martinez",
    "fullName": "Bob Martinez",
    "salary": 95000,
    "age": 35,
    "jobTitle": "Senior Engineer",
    "email": "bob.martinez@company.com",
    "contractHireDate": "2019-06-01T00:00:00Z",
    "contractTerminationDate": null
  },
  {
    "uuid": "6ec0ed70-8c1e-442d-b88c-29923eee7fa5",
    "firstName": "Carol",
    "lastName": "Smith",
    "fullName": "Carol Smith",
    "salary": 72000,
    "age": 27,
    "jobTitle": "QA Engineer",
    "email": "carol.smith@company.com",
    "contractHireDate": "2022-11-10T00:00:00Z",
    "contractTerminationDate": null
  }
]
```

---

### GET /api/v1/employee/{uuid} — Get Employee by UUID

Returns a single employee matching the provided UUID.

**Request:**
```
GET /api/v1/employee/5e8be5bf-b3c3-4d2e-81f6-1cf14ed7cbfa
X-API-Key: rq-secret-key-2024
```

**Response — 200 OK:**
```json
{
  "uuid": "5e8be5bf-b3c3-4d2e-81f6-1cf14ed7cbfa",
  "firstName": "Alice",
  "lastName": "Johnson",
  "fullName": "Alice Johnson",
  "salary": 85000,
  "age": 30,
  "jobTitle": "Software Engineer",
  "email": "alice.johnson@company.com",
  "contractHireDate": "2021-03-15T00:00:00Z",
  "contractTerminationDate": null
}
```

**Response — 404 Not Found** (UUID does not exist):
```json
{
  "timestamp": "2026-04-02T05:18:47.820+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/v1/employee/00000000-0000-0000-0000-000000000000"
}
```

---

### POST /api/v1/employee — Create Employee

Creates a new employee and adds them to the store. Returns the created employee with a generated UUID.

**Request:**
```
POST /api/v1/employee
X-API-Key: rq-secret-key-2024
Content-Type: application/json

{
  "firstName": "Rahul",
  "lastName": "Mourya",
  "salary": 80000,
  "age": 22,
  "jobTitle": "Associate Software Engineer",
  "email": "rahul.mourya@company.com"
}
```

**Response — 201 Created:**
```json
{
  "uuid": "1cb54f7d-55d5-4ba6-ae3c-c77116883f72",
  "firstName": "Rahul",
  "lastName": "Mourya",
  "fullName": "Rahul Mourya",
  "salary": 80000,
  "age": 22,
  "jobTitle": "Associate Software Engineer",
  "email": "rahul.mourya@company.com",
  "contractHireDate": "2026-04-02T05:18:46.830810395Z",
  "contractTerminationDate": null
}
```

**Response — 400 Bad Request** (missing required fields):
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "firstName and lastName are required"
}
```

---

### 401 Unauthorized — Missing or Invalid API Key

Any request without a valid `X-API-Key` header returns:
```json
{
  "error": "Unauthorized: invalid or missing API key"
}
```

---

## Original Challenge Requirements

Please keep the following in mind while working on this challenge:
* Code implementations will not be graded for **correctness** but rather on practicality
* Articulate clear and concise design methodologies, if necessary
* Use clean coding etiquette

### Problem Statement

Your employer has recently purchased a license to top-tier SaaS platform, Employees-R-US, to off-load all employee management responsibilities.
Unfortunately, your company's product has an existing employee management solution that is tightly coupled to other services and therefore
cannot be replaced whole-cloth. Product and Development leads in your department have decided it would be best to interface
the existing employee management solution with the commercial offering from Employees-R-US for the time being until all employees can be
migrated to the new SaaS platform.

Your ask is to expose employee information as a protected, secure REST API for consumption by Employees-R-US web hooks.

### Code Formatting

This project utilizes Gradle plugin [Diffplug Spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) to enforce format and style guidelines with every build.

```bash
./gradlew spotlessApply   # format code
./gradlew build           # build + format check
```
