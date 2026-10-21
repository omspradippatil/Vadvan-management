# Vadhvan Port & Fleet Management System
## Java Spring Boot Micro-Project Architecture & Practical Viva Guide

---

### 1. Executive Project Overview
The **Vadhvan Port & Fleet Management System** is an enterprise-grade logistics and port operations microservice built using **Java 21** and the **Spring Boot 3** ecosystem. 

It handles complex port automation workflows for the upcoming Vadhvan Mega Port:
* **Maritime Operations:** Ship berthing schedules, automated dock allocation, depth draft safety checks, and container offloading.
* **Intermodal Logistics:** Rail-track container allocation and warehouse inventory tracking.
* **Fleet & Driver Dispatch:** Heavy transport vehicles (Trucks, Reach Stackers, Cranes), live trip dispatching, and cargo weight capacity verification.
* **Maintenance & Telemetry:** Preventative maintenance scheduling, fuel logs, operational expenses, and port health diagnostics.
* **Enterprise Security:** Role-Based Access Control (RBAC) and stateless JSON Web Token (JWT) authentication.

---

### 2. Complete Java Technology Stack

| Technology / Library | Version | Purpose in this Project |
| :--- | :--- | :--- |
| **Java (OpenJDK)** | **21 / 27** | Core programming language leveraging modern OOP, Strong Typing, Records, and Streams. |
| **Spring Boot** | **3.3.4** | Production-ready microservice framework providing embedded Tomcat, auto-configuration, and dependency injection. |
| **Spring Data JPA** | **3.3.x** | Abstraction over the database layer; provides out-of-the-box CRUD and query method derivation. |
| **Hibernate ORM** | **6.5.x** | High-performance Object-Relational Mapping (ORM) engine translating Java entity objects into SQL queries. |
| **Spring Security** | **6.3.x** | Enterprise security framework managing the HTTP filter chain, CORS, CSRF, and method authorization. |
| **JJWT (io.jsonwebtoken)** | **0.12.5** | Secure cryptographic signing and parsing of JSON Web Tokens using HMAC-SHA256. |
| **Jakarta Bean Validation** | **3.0.x** | Declarative validation annotations (`@NotBlank`, `@Email`, `@Min`, `@Valid`) on incoming HTTP requests. |
| **PostgreSQL JDBC Driver** | **42.7.x** | Native JDBC driver connecting the Spring Boot backend to the PostgreSQL database. |
| **Apache Maven** | **3.9+** | Build automation tool handling compilation, dependency resolution, packaging (`.jar`), and lifecycle plugins. |

---

### 3. Software Architecture & Enterprise Design Patterns
When your professor or examiner asks: *"What software architecture and design patterns have you used?"*, you can confidently explain these 8 patterns implemented in the code:

```
[ HTTP Request from React Frontend / Postman ]
                     │
                     ▼
       ┌───────────────────────────┐
       │ JwtAuthenticationFilter   │  ◄── Pattern 1: Filter Chain Pattern
       └─────────────┬─────────────┘
                     ▼
       ┌───────────────────────────┐
       │     REST Controller       │  ◄── Pattern 2: Model-View-Controller (MVC)
       └─────────────┬─────────────┘
                     ▼
       ┌───────────────────────────┐
       │      Service Layer        │  ◄── Pattern 3: Service Layer & Business Logic
       └─────────────┬─────────────┘
                     ▼
       ┌───────────────────────────┐
       │   Spring Data Repository  │  ◄── Pattern 4: Repository / DAO Pattern
       └─────────────┬─────────────┘
                     ▼
       ┌───────────────────────────┐
       │   Hibernate & PostgreSQL  │  ◄── Pattern 5: Object-Relational Mapping (ORM)
       └───────────────────────────┘
```

#### 1. MVC (Model-View-Controller) Architecture
* **Model (`com.vadhvan.models`):** Database entities representing business tables (`Vehicle`, `Driver`, `Trip`, `Ship`, `Dock`, etc.).
* **View:** Stateless JSON responses produced by Jackson serialization (and consumed by React frontend).
* **Controller (`com.vadhvan.controllers`):** Exposes RESTful endpoints, handles HTTP routing, and coordinates input validation.

#### 2. Repository Pattern (Data Access Object - DAO)
* Decouples business logic from SQL queries.
* Built using `org.springframework.data.jpa.repository.JpaRepository`.
* Queries are derived automatically from method names (e.g., `findByStatus`, `existsByRegistrationNo`, `countByVehicleId`).

#### 3. Service Layer Pattern
* Located in `com.vadhvan.services`.
* Encapsulates all domain validations, transaction boundaries (`@Transactional`), and port rules (e.g., preventing overweight cargo or blocking deletion of vehicles assigned to active trips).

#### 4. Dependency Injection (DI) & Inversion of Control (IoC)
* Spring's IoC container manages the lifecycle of classes as managed **Spring Beans**.
* We utilize **Constructor-Based Dependency Injection** across all controllers and services, which is the current industry gold standard (ensures immutability and easy unit testing).

#### 5. Data Transfer Object (DTO) Pattern
* Located in `com.vadhvan.dto` (`ApiResponse`, `PaginatedResponse`, `AuthDtos`).
* Decouples the internal database schema from external API contracts. Sensitive fields (like `passwordHash`) are never exposed directly to clients.

#### 6. Filter Chain Pattern (Security Interception)
* Implemented in `JwtAuthenticationFilter` extending `OncePerRequestFilter`.
* Intercepts every incoming HTTP request before it reaches the controller to inspect the `Authorization: Bearer <token>` header.

#### 7. Singleton Pattern
* By default, all Spring-managed beans (`@Service`, `@Repository`, `@RestController`, `PasswordEncoder`) are initialized as thread-safe singletons within the ApplicationContext.

#### 8. Global Exception Handling Pattern
* Implemented in `GlobalExceptionHandler` using `@RestControllerAdvice` and `@ExceptionHandler`.
* Catches unhandled exceptions (`IllegalArgumentException`, `MethodArgumentNotValidException`, `AccessDeniedException`) and formats them into a clean, uniform JSON error response instead of leaking Java stack traces.

---

### 4. Key Java & Spring Annotations Explained (Viva Cheat Sheet)

| Annotation | Where it is used | What it does |
| :--- | :--- | :--- |
| `@SpringBootApplication` | `VadhvanApplication.java` | Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Starts the embedded Tomcat server. |
| `@RestController` | Controllers | Marks the class as a web controller where every method returns a domain object serialized directly into JSON. |
| `@RequestMapping("/api")` | Controllers | Specifies the base URL path prefix for all endpoints in the controller. |
| `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` | Controller methods | Maps specific HTTP request methods (GET, POST, PUT, DELETE) to Java handler methods. |
| `@PathVariable` | Controller methods | Extracts values directly from the URL path (e.g., `/api/vehicles/{id}`). |
| `@RequestBody` | Controller methods | Automatically deserializes the incoming JSON HTTP body into a Java object using Jackson. |
| `@Entity` | Models | Marks a Java class as a JPA database entity mapped to a relational database table. |
| `@Table(name = "vehicles")` | Models | Specifies the exact relational table name in PostgreSQL. |
| `@Id` | Models | Marks the primary key field of an entity. |
| `@Column(name = "\"registrationNo\"")` | Models | Maps the Java field to the exact column name in PostgreSQL. Quoting preserves camelCase in Postgres. |
| `@PrePersist` / `@PreUpdate` | Models | JPA lifecycle callback executed automatically right before `INSERT` or `UPDATE` SQL queries (used for auto-generating UUIDs and setting timestamps). |
| `@Service` | Services | Identifies a business logic service class for Spring component scanning and dependency injection. |
| `@Repository` | Repositories | Marks a data access interface for Spring Data JPA proxy generation and exception translation. |
| `@Transactional` | Service methods | Automatically begins a database transaction on entry, commits it on success, and rolls back if a RuntimeException occurs. |
| `@Configuration` | Config classes | Marks a class that declares one or more `@Bean` methods for the Spring application context. |
| `@Bean` | Config classes | Tells Spring that a method produces an object that should be registered as a Spring bean. |
| `@EnableWebSecurity` | `SecurityConfig` | Activates Spring Security’s web security support and filter chain integration. |
| `@PreAuthorize("hasRole('ADMIN')")` | Admin endpoints | Restricts endpoint access strictly to users having the `ADMIN` role. |

---

### 5. Critical Port Business Logic & Vulnerability Safeguards

During testing, we discovered and fortified critical logic checks inside the Java backend:

1. **Cargo Overweight Validation:**
   * In `CoreService.createTrip` and `updateTrip`, the backend verifies `cargoWeight <= vehicle.getMaxCapacity()`. If a user attempts to load 100 tons into a 40-ton truck, it throws an `IllegalArgumentException` returning `HTTP 400 Bad Request`.
2. **Foreign Key Deletion Crash Safeguard:**
   * In `CoreService.deleteVehicle` and `deleteDriver`, the service checks `tripRepository.countByVehicleId(id)`. If active or past trips exist, it gracefully rejects the deletion with `HTTP 400` instead of letting PostgreSQL crash with a 500 error.
3. **Ship Docking Safety:**
   * In `PortService.dockShip`, it validates that the dock status is `AVAILABLE` and marks both the dock as `OCCUPIED` and the ship as `DOCKED` within a single `@Transactional` boundary.
4. **Dock & Warehouse Protection:**
   * A dock cannot be deleted if a ship is docked at it or if active containers are assigned to it.
   * A warehouse cannot be deleted if active containers are stored inside it.

---

### 6. Authentication & Security Flow (JWT + BCrypt)

```
1. User submits { email, password } to POST /api/auth/login
2. AuthService retrieves User by email from PostgreSQL via UserRepository
3. BCryptPasswordEncoder.matches(rawPassword, storedHash) verifies hash
4. JwtTokenProvider signs an HMAC-SHA256 token containing claims: { id, email, role }
5. Returns { success: true, data: { accessToken: "...", user: {...} } }
6. On subsequent requests:
   - Client sends header: Authorization: Bearer <accessToken>
   - JwtAuthenticationFilter intercepts request
   - Validates cryptographic signature and expiration
   - Injects UserPrincipal into SecurityContextHolder
   - Controller executes with full user identity and role context
```

---

### 7. How to Run the Java Backend Locally

#### Automated Zero-Config Setup
`VadhvanApplication` includes an automatic `.env` loader that discovers your existing database connection string (`DATABASE_URL`) from `backend/.env` or `.env`. You do **not** need to manually export variables!

#### Terminal 1: Run the Spring Boot Java Backend
```bash
cd backend-java
mvn clean package -DskipTests
java -jar target/vadhvan-port-backend-1.0.0.jar
```
*(Or use `mvn spring-boot:run`)*

The Spring Boot embedded Tomcat web server will start up in ~2 seconds on port **5001** (or 5000 based on your `.env` setting).

#### 💡 If Port 5001 or 5000 is Already Occupied (Kill Command):
If an old process or previous server is already using the port, kill it with a single command:

* **macOS / Linux:**
  ```bash
  # Check which process is using the port:
  lsof -i :5001

  # Force kill the process holding port 5001:
  kill -9 $(lsof -t -i:5001)

  # (Or to kill both 5000 and 5001):
  kill -9 $(lsof -t -i:5000 -i:5001)
  ```

* **Windows:**
  ```cmd
  # Find PID:
  netstat -ano | findstr :5001
  # Kill PID:
  taskkill /PID <PID_NUMBER> /F
  ```

#### Terminal 2: Run the React Frontend
```bash
cd frontend
npm run dev
```
Open [http://localhost:5173](http://localhost:5173) in your browser. The Vite development proxy forwards all `/api/*` requests directly to your running Java backend!

#### Demo Credentials for Testing / Viva:
* **Email:** `admin@vadhvanport.in`
* **Password:** `Admin@123`
*(Returns a cryptographically signed HMAC-SHA256 JWT token and full ADMIN privileges)*

---

### 8. Top 15 Viva / Practical Questions & Direct Answers

**Q1: What is Spring Boot and why did you choose it over standard Java Servlets?**
> **Answer:** Spring Boot provides a production-ready framework with an embedded Tomcat server, opinionated default configurations, and automated dependency management via Starters. It eliminates boilerplate XML configuration and allows rapid REST API development with built-in dependency injection.

**Q2: What is the difference between `@Component`, `@Service`, and `@Repository`?**
> **Answer:** All three are stereotypes of `@Component` discovered by Spring's component scanning. `@Service` semantically indicates business logic and transaction boundaries. `@Repository` indicates the Data Access Object (DAO) layer and provides automatic translation of database-specific SQL exceptions into Spring’s `DataAccessException` hierarchy.

**Q3: How does Spring Data JPA execute queries without us writing SQL?**
> **Answer:** Spring Data JPA dynamically generates query implementations at runtime based on method naming conventions (like `findByRegistrationNo` or `countByVehicleId`) using JPA Criteria API and Hibernate under the hood.

**Q4: What is Inversion of Control (IoC) and Dependency Injection (DI)?**
> **Answer:** Inversion of Control means the control of object creation and lifecycle is inverted from the programmer to the Spring container. Dependency Injection is the mechanism used to implement IoC, where dependencies are supplied (injected) to a class rather than the class creating them itself.

**Q5: Why did you use Constructor Injection instead of `@Autowired` on fields?**
> **Answer:** Field injection makes classes tightly coupled to the Spring container, hides dependencies, and prevents making fields `final`. Constructor injection guarantees that required dependencies cannot be null, promotes immutability, and makes unit testing simple without needing reflection.

**Q6: How does JWT (JSON Web Token) authentication work in a stateless API?**
> **Answer:** JWT is a cryptographically signed JSON string containing claims (such as user ID and role). Unlike traditional session IDs stored in server memory or Redis, the server does not store the JWT. Every request contains the token, and the server validates its integrity using a secret key with HMAC-SHA256. This enables horizontal scalability and zero session storage overhead.

**Q7: What is BCrypt and why is it used for passwords?**
> **Answer:** BCrypt is an adaptive, one-way cryptographic hashing function incorporating a work factor (salt rounds). It includes automatic salting to protect against rainbow table attacks and is computationally slow by design to protect against brute-force attacks.

**Q8: What does `@Transactional` do?**
> **Answer:** It defines the scope of a single database transaction. If the method completes normally, Spring commits the transaction. If a `RuntimeException` is thrown, Spring automatically rolls back all database modifications performed within that method, preventing inconsistent data.

**Q9: How do you prevent Foreign Key crashes in your delete endpoints?**
> **Answer:** Before issuing a `deleteById` call, our service queries child tables using repository count methods (e.g. `tripRepository.countByVehicleId(id)`). If linked records exist, it throws an `IllegalArgumentException` caught by `@RestControllerAdvice`, returning a graceful HTTP 400 Bad Request error to the client instead of crashing with a database constraint error.

**Q10: What is Hibernate and what role does it play?**
> **Answer:** Hibernate is the Object-Relational Mapping (ORM) provider implementing the JPA (Jakarta Persistence API) specification. It maps Java objects (Entities) to relational database tables and handles query generation, caching, and dirty checking.

**Q11: What is the difference between `@RestController` and `@Controller`?**
> **Answer:** `@Controller` is used for traditional Spring MVC web applications that return view templates (like Thymeleaf or JSP). `@RestController` is a convenience annotation combining `@Controller` and `@ResponseBody`, meaning every method returns data serialized directly into JSON or XML.

**Q12: How does the CORS filter work in Spring Security?**
> **Answer:** In `SecurityConfig`, we define a `CorsConfigurationSource` bean specifying allowed origins, HTTP methods (GET, POST, PUT, DELETE), and headers. This ensures browsers allow cross-origin requests from our React frontend while blocking unauthorized third-party domains.

**Q13: What does `@PrePersist` and `@PreUpdate` do in your entity models?**
> **Answer:** They are JPA entity lifecycle callback annotations. Methods annotated with `@PrePersist` execute automatically before an entity is first saved to the database (used to generate UUIDs and set `createdAt`). `@PreUpdate` executes before an existing record is updated (to refresh `updatedAt`).

**Q14: How does your application handle global errors?**
> **Answer:** We use `@RestControllerAdvice` in `GlobalExceptionHandler`. It intercepts exceptions thrown anywhere across controllers and services, translating them into structured JSON (`ApiResponse.error(...)`) with standard HTTP status codes (400, 401, 403, 500).

**Q15: Can this Java backend be deployed to cloud platforms?**
> **Answer:** Yes! Because Spring Boot packages into a self-contained executable `.jar` file with an embedded Tomcat server, it can be deployed on Docker, AWS EC2/ECS, Render, Heroku, or Kubernetes by simply executing `java -jar app.jar`.
