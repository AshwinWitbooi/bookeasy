# GitHub Copilot Instructions – BookEasy (Spring Boot Edition)

## 1. Project Context & Purpose
**BookEasy** is a centralized booking and reservation web application for a small service-based business (e.g., meeting rooms, consultations, client appointments). It replaces spreadsheets and emails with a single cohesive **Java 21 + Spring Boot** application combining:
- **Frontend:** Server-side **Spring Thymeleaf** templates (`HTML5`, `CSS3`, vanilla `JavaScript` with Fetch API where appropriate). **Do NOT introduce React, Angular, Vue, TypeScript, or Node.js.**
- **Backend:** Layered Spring Boot application exposing both **Spring MVC** web routes (`@Controller`) and **RESTful JSON APIs** (`@RestController`).
- **Persistence:** **Spring Data JPA (Hibernate)** backed by an embedded **H2** relational database.

---

## 2. Explicit Scope Guardrails (DO NOT IMPLEMENT)
To preserve v1 scope boundaries and keep the codebase focused, **NEVER** generate or add:
- **Security, Authentication, or Authorization:** Exclude `spring-boot-starter-security`, login/logout forms, JWT/OAuth2, CSRF tokens, password encoding, or role-based access control (RBAC). All web routes and REST endpoints are openly accessible in v1.
- **Excluded v1 Features:** Online payments/gateways, email/SMS notifications, multiple business locations, calendar synchronization, recurring bookings, advanced reporting/analytics, mobile apps, and real-time availability/overlapping booking prevention.

---

## 3. Package & Directory Structure (`com.example.bookeasy`)
Strictly place new and modified files in their designated packages:

```text
src/
 ├── main/
 │   ├── java/com/example/bookeasy/
 │   │   ├── BookEasyApplication.java
 │   │   ├── config/          # WebConfig and general bean configuration (NO SecurityConfig)
 │   │   ├── controller/
 │   │   │   ├── web/         # BookingWebController (@Controller -> Thymeleaf views)
 │   │   │   └── api/         # BookingRestController (@RestController -> /api/bookings)
 │   │   ├── service/         # BookingService (@Service, @Transactional business logic)
 │   │   ├── repository/      # BookingRepository (extends JpaRepository<Booking, Long>)
 │   │   ├── entity/          # Booking JPA entity & BookingStatus enum (CONFIRMED, CANCELLED)
 │   │   ├── dto/             # BookingCreateRequest, BookingUpdateRequest, BookingResponse, ErrorResponse
 │   │   ├── mapper/          # BookingMapper (DTO <-> Entity conversions)
 │   │   ├── exception/       # BookingNotFoundException & ApiExceptionHandler (@RestControllerAdvice)
 │   │   └── validation/      # Custom Jakarta validators (if needed)
 │   └── resources/
 │       ├── templates/
 │       │   ├── fragments/   # head.html, navbar.html, alerts.html
 │       │   └── pages/bookings/ # list.html, detail.html, form.html
 │       ├── static/          # css/styles.css, js/bookings.js, images/
 │       ├── application.properties
 │       └── data.sql         # Idempotent seed data
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

## 4. Architectural & Coding Standards

### Layer Responsibilities (Strict Separation of Concerns)
1. **Controllers (`controller.web` & `controller.api`):**
   - Keep controllers thin. Never put business rules or direct repository calls inside controllers.
   - `BookingWebController` handles browser navigation, Post-Redirect-Get (`302` redirects with `RedirectAttributes` flash alerts), and Thymeleaf form binding (`@Valid`, `BindingResult`).
   - `BookingRestController` exposes `/api/bookings` endpoints, accepts validated DTOs, and returns `ResponseEntity<BookingResponse>` with accurate HTTP status codes (`200 OK`, `201 Created` + `Location` header, `204 No Content`, `400 Bad Request`, `404 Not Found`).
2. **DTOs & Mappers (`dto/` & `mapper/`):**
   - **Never expose JPA entities (`Booking`) directly in REST API responses or accept them as request bodies.**
   - Use `BookingCreateRequest`, `BookingUpdateRequest`, `BookingResponse`, and `ErrorResponse`.
   - Centralize conversion logic in `BookingMapper`.
3. **Service Layer (`service/`):**
   - Annotate `BookingService` with `@Service` and `@Transactional(readOnly = true)` at the class level; annotate write operations (`createBooking`, `updateBooking`, `cancelBooking`, `deleteBooking`) with `@Transactional`.
   - Enforce domain rules: new bookings default to `BookingStatus.CONFIRMED`; soft cancellation transitions status to `BookingStatus.CANCELLED` and updates `updatedAt`; hard deletion removes the entity; missing IDs throw `BookingNotFoundException`.
4. **Repository & Persistence (`repository/` & `entity/`):**
   - Use Spring Data JPA (`JpaRepository<Booking, Long>`) with derived queries, sorting, and pagination. Avoid raw JDBC.
   - `Booking` maps to table `BOOKINGS` with columns: `id` (`BIGINT` PK), `customer_name` (`VARCHAR(120)`, `NOT NULL`), `customer_email` (`VARCHAR(180)`, `NOT NULL`, indexed), `booking_date` (`DATE`, `NOT NULL`, indexed), `booking_time` (`TIME`, `NOT NULL`), `description` (`VARCHAR(500)`), `status` (`VARCHAR(20)`, `@Enumerated(EnumType.STRING)`), `created_at` (`TIMESTAMP`), and `updated_at` (`TIMESTAMP`).
5. **Exception Handling (`exception/`):**
   - Centralize REST error responses in `ApiExceptionHandler` (`@RestControllerAdvice`), mapping `MethodArgumentNotValidException` to `400 Bad Request` and `BookingNotFoundException` to `404 Not Found` using `ErrorResponse`.

### Validation Rules (Jakarta Bean Validation)
Apply these exact constraints on request DTOs:
- `customerName`: `@NotBlank(message = "Customer name is required")`, `@Size(max = 120)`
- `customerEmail`: `@NotBlank(message = "Customer email is required")`, `@Email(message = "Customer email must be valid")`, `@Size(max = 180)`
- `bookingDate`: `@NotNull(message = "Booking date is required")`, `@FutureOrPresent(message = "Booking date cannot be in the past")`
- `bookingTime`: `@NotNull(message = "Booking time is required")`
- `description`: `@Size(max = 500, message = "Description must not exceed 500 characters")`

### Frontend Conventions (Thymeleaf, CSS, JS)
- Reuse `fragments/head.html`, `fragments/navbar.html`, and `fragments/alerts.html` across pages.
- Always use `th:text` (never `th:utext`) when rendering user-supplied data to prevent XSS.
- Use `th:object`, `th:field`, and `#fields.hasErrors(...)` / `th:errors` for form binding and field-level validation feedback.
- Keep JavaScript in `static/js/bookings.js` vanilla and lightweight (e.g., confirmation dialogs before cancel/delete actions).

---

## 5. Development & Testing Workflow for AI Agents
1. **Inspect First:** Always read `pom.xml`, `application.properties`, existing classes, templates, and tests before generating or modifying code.
2. **Incremental Implementation:** Implement one feature or vertical slice at a time following the ordered backlog in `ISSUES.md` (`Issue #1` through `Issue #10`). Do not rewrite unrelated working files.
3. **Mandatory Testing:** Every feature or bug fix must include automated tests using **JUnit 5** and **Mockito** (`BookingServiceTest`), `@DataJpaTest` (`BookingRepositoryTest`), and `MockMvc` (`BookingRestControllerTest` / `BookingWebControllerTest`).
4. **Verify Before Finishing:** Run `mvn clean test` and confirm all unit, slice, and integration tests pass with zero compilation or runtime errors.