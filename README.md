# BookEasy – Simple Booking & Reservation Application

**BookEasy** is a centralized booking and reservation web application built for small service-based businesses—such as managing meeting rooms, consultation sessions, or client appointments. It replaces fragmented spreadsheets, email threads, and messaging logs with a cohesive **Java Spring Boot** application combining server-rendered **Spring Thymeleaf** pages, **RESTful JSON APIs**, and **Spring Data JPA** persistence over an embedded **H2** relational database.

---

## Table of Contents

- [Core Features (v1 Scope)](#core-features-v1-scope)
- [Technology Stack](#technology-stack)
- [System & Application Architecture](#system--application-architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Web UI Routes & REST API Endpoints](#web-ui-routes--rest-api-endpoints)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Project Documentation & Backlog](#project-documentation--backlog)
- [AI-Assisted Development Guidelines](#ai-assisted-development-guidelines)
- [Explicit v1 Scope Exclusions](#explicit-v1-scope-exclusions)

---

## Core Features (v1 Scope)

BookEasy implements five core reservation workflows for **Customers** (create and view bookings) and **Administrators** (view, update, cancel, and delete bookings):

1. **Create Booking (`FEAT-01` / `UC-01`):** Capture customer name, email, booking date, booking time, and optional description with Jakarta Bean Validation (`@NotBlank`, `@Email`, `@NotNull`, `@FutureOrPresent`, `@Size`) and persist with initial status `CONFIRMED`.
2. **View Bookings List (`FEAT-02` / `UC-02`):** Display all existing reservations in a responsive table ordered for operational visibility, complete with status badges (`CONFIRMED` / `CANCELLED`), row actions, and an empty-state prompt.
3. **View Booking Details (`FEAT-03` / `UC-03`):** Inspect individual booking attributes alongside audit timestamps (`createdAt`, `updatedAt`) and management actions.
4. **Update Booking (`FEAT-04` / `UC-04`):** Modify existing booking details with full validation enforcement and automatic `updatedAt` timestamp refresh.
5. **Cancel or Delete Booking (`FEAT-05` / `UC-05`):** Soft-cancel a booking by transitioning its status to `CANCELLED`, or permanently remove the record from the database, protected by client-side confirmation prompts.

---

## Technology Stack

| Layer / Concern | Technology | Purpose |
| :--- | :--- | :--- |
| **Language** | Java 21 (LTS) | Primary backend programming language |
| **Backend Framework** | Spring Boot (Spring MVC + REST) | Unified web and RESTful JSON API runtime |
| **Frontend** | Spring Thymeleaf, HTML5, CSS3, Vanilla JS | Server-side rendered views, reusable fragments, form binding, and progressive Fetch API UX |
| **ORM / Data Access** | Spring Data JPA with Hibernate | Repository abstractions, pagination/sorting, and `@Transactional` boundaries |
| **Database** | H2 Relational Database | Embedded SQL database with schema generation and idempotent `data.sql` seeding |
| **Validation** | Jakarta Bean Validation (`jakarta.validation`) | Declarative DTO and form input constraints |
| **Build Tool** | Apache Maven | Dependency management, build lifecycle, and test execution |
| **Testing** | JUnit 5, Mockito, Spring Test (`MockMvc`, `@DataJpaTest`) | Unit, repository slice, controller, and full-context integration testing |
| **Version Control & SDLC** | Git, GitHub, AI Coding Agents | Incremental, issue-driven development and code review |

---

## System & Application Architecture

BookEasy follows a **layered Spring Boot architecture** with strict unidirectional dependencies and DTO encapsulation (JPA entities are never exposed directly in REST responses):

```text
+-----------------------------------------------------------------------+
|                    Presentation & Transport Layer                     |
|   Thymeleaf Templates (fragments/, pages/bookings/)                   |
|   BookingWebController (@Controller)  |  BookingRestController (@Rest) |
|   ApiExceptionHandler (@RestControllerAdvice -> ErrorResponse JSON)    |
+-----------------------------------+-----------------------------------+
                                    | Validated Request / Response DTOs
                                    v
+-----------------------------------------------------------------------+
|                   Data Transfer & Service Layer                       |
|   BookingService (@Service, @Transactional)                           |
|   BookingMapper (Bidirectional DTO <-> Entity Conversion)             |
+-----------------------------------+-----------------------------------+
                                    | Domain Entities
                                    v
+-----------------------------------------------------------------------+
|                    Repository & Persistence Layer                     |
|   BookingRepository (JpaRepository<Booking, Long>)                    |
|   Booking Entity & BookingStatus Enum  <-->  H2 Database (BOOKINGS)   |
+-----------------------------------------------------------------------+
```

---

## Project Structure

```text
bookeasy/
 ├── pom.xml
 ├── README.md
 ├── FEATURES.md
 ├── USECASES.md
 ├── ISSUES.md
 └── src/
     ├── main/
     │   ├── java/com/example/bookeasy/
     │   │   ├── BookEasyApplication.java
     │   │   ├── config/          # Web and application configuration
     │   │   ├── controller/
     │   │   │   ├── web/         # BookingWebController (Thymeleaf MVC routes)
     │   │   │   └── api/         # BookingRestController (/api/bookings JSON endpoints)
     │   │   ├── service/         # BookingService (@Transactional business logic)
     │   │   ├── repository/      # BookingRepository (Spring Data JPA)
     │   │   ├── entity/          # Booking JPA entity & BookingStatus enum
     │   │   ├── dto/             # Create/Update Request DTOs, BookingResponse, ErrorResponse
     │   │   ├── mapper/          # BookingMapper (DTO <-> Entity mapping)
     │   │   ├── exception/       # BookingNotFoundException & ApiExceptionHandler
     │   │   └── validation/      # Custom validation annotations/validators
     │   └── resources/
     │       ├── templates/
     │       │   ├── fragments/   # head.html, navbar.html, alerts.html
     │       │   └── pages/bookings/ # list.html, detail.html, form.html
     │       ├── static/          # css/styles.css, js/bookings.js, images/
     │       ├── application.properties
     │       └── data.sql         # Idempotent sample seed data
     └── test/
         └── java/com/example/bookeasy/
             ├── service/         # BookingServiceTest (JUnit 5 + Mockito)
             ├── repository/      # BookingRepositoryTest (@DataJpaTest)
             ├── controller/
             │   ├── api/         # BookingRestControllerTest (MockMvc)
             │   └── web/         # BookingWebControllerTest (MockMvc)
             └── BookEasyApplicationTests.java
```

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK):** Java 21 or higher
- **Build Tool:** Apache Maven 3.9+ (or included `./mvnw` wrapper)
- **Version Control:** Git

### Local Setup & Execution

1. **Clone the repository:**
   ```bash
   git clone https://github.com/<your-org>/bookeasy.git
   cd bookeasy
   ```

2. **Build and run the automated test suite:**
   ```bash
   mvn clean test
   ```

3. **Start the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application:**
   - **Web UI:** `http://localhost:8080/bookings`
   - **REST API Base URL:** `http://localhost:8080/api/bookings`
   - **H2 Console (if enabled in local profile):** `http://localhost:8080/h2-console`

---

## Web UI Routes & REST API Endpoints

### Thymeleaf Web Routes (`BookingWebController`)

| Method & Route | Template | Description |
| :--- | :--- | :--- |
| `GET /` or `GET /bookings` | `pages/bookings/list.html` | View all bookings with status badges and actions |
| `GET /bookings/new` | `pages/bookings/form.html` | Render new booking creation form |
| `POST /bookings` | Redirects to `/bookings` | Validate and create a new booking (PRG pattern) |
| `GET /bookings/{id}` | `pages/bookings/detail.html` | View individual booking details and timestamps |
| `GET /bookings/{id}/edit` | `pages/bookings/form.html` | Render pre-populated edit booking form |
| `POST /bookings/{id}` | Redirects to `/bookings/{id}` | Validate and save booking updates |
| `POST /bookings/{id}/cancel` | Redirects to `/bookings` | Soft-cancel booking (`status = CANCELLED`) |
| `POST /bookings/{id}/delete` | Redirects to `/bookings` | Permanently delete booking record |

### RESTful JSON Endpoints (`BookingRestController`)

| Method & Endpoint | Request Payload / Params | Success Response | Error Responses |
| :--- | :--- | :--- | :--- |
| `GET /api/bookings` | Optional `page`, `size`, `sort` | `200 OK` (List/Page of `BookingResponse`) | `400 Bad Request` |
| `POST /api/bookings` | `BookingCreateRequest` | `201 Created` + `Location` + `BookingResponse` | `400 Bad Request` |
| `GET /api/bookings/{id}` | Path variable `id` | `200 OK` (`BookingResponse`) | `404 Not Found` |
| `PUT /api/bookings/{id}` | Path `id` + `BookingUpdateRequest` | `200 OK` (`BookingResponse`) | `400 Bad Request`, `404 Not Found` |
| `PATCH /api/bookings/{id}/cancel` | Path variable `id` | `200 OK` (`BookingResponse`) | `404 Not Found` |
| `DELETE /api/bookings/{id}` | Path variable `id` | `204 No Content` | `404 Not Found` |

---

## Database Schema

Table: **`BOOKINGS`** (H2 Relational Database)

| Column | Java Type | SQL Type | Constraints & Notes |
| :--- | :--- | :--- | :--- |
| `id` | `Long` | `BIGINT` | Primary Key, auto-increment (`GenerationType.IDENTITY`) |
| `customer_name` | `String` | `VARCHAR(120)` | `NOT NULL`, `@NotBlank`, max 120 chars |
| `customer_email` | `String` | `VARCHAR(180)` | `NOT NULL`, `@Email`, indexed (`idx_bookings_customer_email`) |
| `booking_date` | `LocalDate` | `DATE` | `NOT NULL`, `@FutureOrPresent`, indexed (`idx_bookings_booking_date`) |
| `booking_time` | `LocalTime` | `TIME` | `NOT NULL` |
| `description` | `String` | `VARCHAR(500)` | Nullable, max 500 chars |
| `status` | `BookingStatus` | `VARCHAR(20)` | `NOT NULL`, `CONFIRMED` or `CANCELLED` |
| `created_at` | `Instant` | `TIMESTAMP` | `NOT NULL`, populated via `@PrePersist` |
| `updated_at` | `Instant` | `TIMESTAMP` | `NOT NULL`, populated via `@PrePersist` / `@PreUpdate` |

---

## Testing

BookEasy enforces automated verification across every architectural layer:
- **Unit Tests (`BookingServiceTest`):** Tests business rules, status transitions, validation rejections, and `BookingNotFoundException` using JUnit 5 and Mockito.
- **Repository Tests (`BookingRepositoryTest`):** Tests JPA entity mapping, constraints, sorting, and pagination against H2 using `@DataJpaTest`.
- **REST & Web Controller Tests (`BookingRestControllerTest`, `BookingWebControllerTest`):** Verifies HTTP status codes, JSON DTO payloads, `@RestControllerAdvice` error responses, Thymeleaf view rendering, and form validation binding with `MockMvc`.
- **Integration Smoke Test (`BookEasyApplicationTests`):** Validates full Spring context startup and `data.sql` initialization via `@SpringBootTest`.

Run all tests with:
```bash
mvn clean test
```

---

## Project Documentation & Backlog

This repository includes structured engineering artifacts designed for human developers and AI coding agents:
- **`FEATURES.md`:** Core v1 feature specifications (`FEAT-01` through `FEAT-05`) and scope guardrails.
- **`USECASES.md`:** Detailed actor workflows, preconditions, main flows, and exception paths (`UC-01` through `UC-05`).
- **`ISSUES.md`:** Dependency-ordered GitHub Project issue backlog (`Issue #1` through `Issue #10`) spanning project setup, core CRUD features, and final QA.

---

## AI-Assisted Development Guidelines

BookEasy is designed for incremental, agent-assisted software development:
1. **Inspect Before Editing:** Review `pom.xml`, existing packages, templates, and tests before modifying code.
2. **Work Issue by Issue:** Implement one atomic issue from `ISSUES.md` per branch/PR rather than generating large, unverified changesets.
3. **Preserve Layer Boundaries:** Keep controllers thin, isolate business rules and `@Transactional` boundaries in `BookingService`, and map all REST payloads through DTOs.
4. **Verify Every Change:** Add or update tests alongside each feature and ensure `mvn clean test` passes before merging.

---

## Explicit v1 Scope Exclusions

To keep v1 focused on the core booking CRUD lifecycle, the following features are explicitly excluded:
- Online payments and payment gateways
- Email and SMS notifications
- Multiple business locations
- Complex user roles and permissions (RBAC)
- Calendar synchronization or recurring bookings
- Advanced reporting and analytics
- Mobile application development
- Real-time availability management and overlapping booking prevention