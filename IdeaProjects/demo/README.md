# FLEMAN - Fleet Management Backend (Java/Spring Boot)

A comprehensive vehicle rental management system built with **Spring Boot 4.0.1** and **Java 17**, featuring booking management, smart rental pricing, Razorpay payment integration, Google SSO authentication, and microservice architecture.

---

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Technology Stack](#technology-stack)
4. [API Endpoints Reference](#api-endpoints-reference)
5. [Core Java Concepts](#core-java-concepts)
6. [Advanced Java Concepts](#advanced-java-concepts)
7. [Spring Boot Concepts](#spring-boot-concepts)
8. [Authentication & Authorization](#authentication--authorization)
9. [Business Logic & Flows](#business-logic--flows)
10. [Payment Integration](#payment-integration)
11. [Interview Preparation](#interview-preparation)

---

## 🚀 Project Overview

FLEMAN is a full-stack fleet management system that allows:

- **Customers**: Browse vehicles, make bookings, and pay online
- **Staff**: Process vehicle pickups (handovers) and returns at hubs
- **Admins**: Manage inventory, users, offers, and system configuration

### Key Features

| Feature | Description |
|---------|-------------|
| **Smart Rental Pricing** | Greedy algorithm optimizes pricing using monthly/weekly/daily rates |
| **Razorpay Integration** | Secure payment processing with HMAC signature verification |
| **Google SSO** | OAuth 2.0 login with automatic user creation |
| **Role-Based Authorization** | JWT-based access for customers, staff, and admins |
| **PDF Invoice Generation** | Automatic invoice PDF with email delivery using iText |
| **AOP Logging** | Cross-cutting concerns handled via Aspect-Oriented Programming |
| **Spring Data JPA** | Repository pattern with MySQL database |

---

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              FRONTEND (Next.js)                              │
│                           http://localhost:3000                              │
└─────────────────────────────────────┬───────────────────────────────────────┘
                                      │ REST API
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                       SPRING BOOT BACKEND (Java 17)                          │
│                         http://localhost:8080                                │
│                                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │ Controllers │→ │  Services   │→ │Repositories │→ │  Database   │        │
│  │  (REST)     │  │ (Business)  │  │ (JPA)       │  │  (MySQL)    │        │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                                              │
│  ┌───────────────────────────────────────────────────────────────┐          │
│  │                     CROSS-CUTTING CONCERNS                     │          │
│  │  • Security Filter Chain  • AOP Logging  • Exception Handler  │          │
│  └───────────────────────────────────────────────────────────────┘          │
└─────────────────────────────────────┬───────────────────────────────────────┘
                                      │
            ┌─────────────────────────┴────────────────────────┐
            ▼                                                  ▼
┌───────────────────────────┐              ┌─────────────────────────────────┐
│   MICROSERVICE (.NET)      │              │        EXTERNAL SERVICES        │
│  http://localhost:5123     │              │                                 │
│                            │              │  • Razorpay Payment Gateway     │
│  POST /api/hubs/upload     │              │  • Google OAuth 2.0             │
│                            │              │  • SMTP Email (Gmail)           │
└───────────────────────────┘              └─────────────────────────────────┘
```

### Layered Architecture (N-Tier)

```
┌───────────────────────────────────────────────────────────────┐
│                    CONTROLLERS (REST Layer)                    │
│  @RestController - Handle HTTP requests, validation, routing  │
│  BookingController, AuthController, PaymentController          │
└───────────────────────────────┬───────────────────────────────┘
                                │ @Autowired
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                    SERVICES (Business Logic)                   │
│  @Service - Core business rules, calculations, orchestration  │
│  BookingService, InvoiceService, PaymentService                │
└───────────────────────────────┬───────────────────────────────┘
                                │ @Autowired
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                   REPOSITORIES (Data Access)                   │
│  @Repository - Spring Data JPA interfaces                     │
│  BookingRepository, VehicleRepository, UserAuthRepository      │
└───────────────────────────────┬───────────────────────────────┘
                                │ Hibernate
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                      ENTITIES (Domain Models)                  │
│  @Entity - JPA mappings to database tables                    │
│  Booking, Vehicle, UserAuth, InvoiceHeader                     │
└───────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technology Stack

| Category | Technology |
|----------|------------|
| **Runtime** | Java 17 (LTS) |
| **Framework** | Spring Boot 4.0.1 |
| **ORM** | Spring Data JPA + Hibernate |
| **Database** | MySQL 8.0 |
| **Security** | Spring Security + JWT (jjwt 0.12.3) |
| **Build Tool** | Maven |
| **PDF Generation** | iText 2.1.7 |
| **Email** | Spring Boot Mail (Gmail SMTP) |
| **File Processing** | Apache POI 5.2.3 (Excel) |
| **OAuth** | Spring OAuth2 Client |
| **Payment** | Razorpay Java SDK 1.4.3 |
| **Monitoring** | Spring Actuator |
| **AOP** | Spring AOP |
| **Code Generation** | Lombok |

---

## 📡 API Endpoints Reference

### Authentication & Users

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/api/auth/login` | User login with email/password | ❌ |
| `POST` | `/api/auth/register` | New user registration | ❌ |
| `POST` | `/api/auth/forgot-password` | Send password reset email | ❌ |
| `POST` | `/api/auth/reset-password` | Reset password with token | ❌ |
| `GET` | `/oauth2/authorization/google` | Initiate Google SSO | ❌ |
| `GET` | `/api/users/{id}` | Get user by ID | ✅ User |
| `PUT` | `/api/users/{id}` | Update user details | ✅ User |

### Bookings

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/api/bookings` | Create new booking | ❌ |
| `GET` | `/api/bookings/{id}` | Get booking by ID | ✅ User |
| `GET` | `/api/bookings` | Get all bookings | ✅ Admin |
| `GET` | `/api/bookings/user/{userId}` | Get user's bookings | ✅ User |
| `GET` | `/api/bookings/hub/{hubId}` | Get bookings by hub | ✅ Staff |
| `PATCH` | `/api/bookings/{id}/status` | Update booking status | ✅ Staff |
| `DELETE` | `/api/bookings/{id}` | Cancel booking | ✅ Admin |

### Handovers & Invoices

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/api/handovers` | Process vehicle pickup | ✅ Staff |
| `POST` | `/api/handovers/return` | Process vehicle return | ✅ Staff |
| `POST` | `/api/invoices/return` | Generate invoice on return | ✅ Staff |
| `GET` | `/api/invoices/{id}` | Get invoice by ID | ✅ User |

### Payments

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/api/payments/create-order` | Create Razorpay order | ✅ User |
| `POST` | `/api/payments/verify` | Verify payment signature | ✅ User |

### Vehicles & Locations

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/api/vehicles/types` | Get all vehicle types | ❌ |
| `GET` | `/api/vehicles/catalog` | Get vehicle catalog | ❌ |
| `GET` | `/api/locations/states` | Get all states | ❌ |
| `GET` | `/api/locations/cities/{stateId}` | Get cities by state | ❌ |
| `GET` | `/api/locations/hubs/{cityId}` | Get hubs by city | ❌ |
| `POST` | `/api/locations/hubs/upload` | Upload hubs from Excel | ✅ Staff |

---

## ☕ Core Java Concepts

### 1. Object-Oriented Programming (OOP)

```java
// ENCAPSULATION - Private fields with public getters/setters
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;  // Private field
    
    private String status;
    
    // Getters/Setters (or use @Data from Lombok)
    public Long getBookingId() { return bookingId; }
    public void setStatus(String status) { this.status = status; }
}

// INHERITANCE - Vehicle extends common behavior
public class Vehicle extends BaseEntity { // Inherits createdAt, updatedAt
    private String registrationNumber;
}

// POLYMORPHISM - Same method, different implementations
public interface PaymentGateway {
    PaymentResult processPayment(Order order);
}
public class RazorpayGateway implements PaymentGateway {
    @Override
    public PaymentResult processPayment(Order order) { /* Razorpay logic */ }
}
public class StripeGateway implements PaymentGateway {
    @Override
    public PaymentResult processPayment(Order order) { /* Stripe logic */ }
}

// ABSTRACTION - Hide implementation details
public abstract class NotificationService {
    public abstract void send(String recipient, String message);
}
public class EmailNotificationService extends NotificationService {
    @Override
    public void send(String recipient, String message) { /* Email logic */ }
}
```

### 2. Java Collections Framework

```java
// LIST - Ordered, allows duplicates
List<Booking> bookings = new ArrayList<>();
bookings.add(new Booking());
bookings.get(0);  // Access by index

// SET - Unique elements, no duplicates
Set<String> statuses = new HashSet<>();
statuses.add("reserved");
statuses.add("active");

// MAP - Key-value pairs
Map<Long, Vehicle> vehicleCache = new HashMap<>();
vehicleCache.put(1L, vehicle);
Vehicle v = vehicleCache.get(1L);

// QUEUE - FIFO ordering
Queue<BookingRequest> requestQueue = new LinkedList<>();
requestQueue.offer(request);
BookingRequest next = requestQueue.poll();

// In our project:
List<BookingResponseDTO> responses = bookings.stream()
    .map(this::buildBookingResponse)
    .collect(Collectors.toList());
```

### 3. Generics

```java
// Generic class - Type-safe API responses
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;  // Can be any type!
    
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }
}

// Usage:
ApiResponseDTO<BookingResponseDTO> response = 
    ApiResponseDTO.success("Booking created", bookingDto);

ApiResponseDTO<List<VehicleDTO>> vehicleResponse = 
    ApiResponseDTO.success("Vehicles found", vehicleList);
```

### 4. Exception Handling

```java
// Custom Exception
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long id) {
        super("Booking not found with ID: " + id);
    }
}

// Try-Catch-Finally
public BookingResponseDTO getBooking(Long id) {
    try {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new BookingNotFoundException(id));
    } catch (BookingNotFoundException e) {
        logger.error("Booking not found: {}", id);
        throw e;  // Re-throw for global handler
    } finally {
        // Always executed - cleanup resources
    }
}

// Global Exception Handler (@ControllerAdvice)
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(BookingNotFoundException e) {
        return ResponseEntity.status(404)
            .body(ApiResponseDTO.error(e.getMessage()));
    }
}
```

### 5. Interfaces and Abstract Classes

```java
// INTERFACE - Contract definition (100% abstract before Java 8)
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(String status);
    List<Booking> findByUserAuth_UserId(Long userId);
    
    // Default method (Java 8+)
    default List<Booking> findActiveBookings() {
        return findByStatus("active");
    }
}

// ABSTRACT CLASS - Partial implementation
public abstract class BaseEntity {
    @Column(name = "created_at")
    private Instant createdAt;
    
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract void validate();
}

// Key Differences:
// Interface: Can implement multiple, no state (before Java 8)
// Abstract: Single inheritance, can have state and constructors
```

### 6. Access Modifiers

```java
public class Vehicle {
    public String name;           // Accessible everywhere
    protected String category;    // Same package + subclasses
    String fuel;                  // Package-private (default)
    private Long vehicleId;       // Only within this class
    
    // Private + Getter = Controlled access (Encapsulation)
    public Long getVehicleId() {
        return vehicleId;
    }
}
```

---

## 🚀 Advanced Java Concepts

### 1. Stream API (Java 8+)

```java
// Convert List to DTOs with filtering and mapping
List<BookingResponseDTO> activeBookings = bookings.stream()
    .filter(b -> "active".equals(b.getStatus()))      // Filter
    .map(this::buildBookingResponse)                   // Transform
    .sorted(Comparator.comparing(BookingResponseDTO::getPickupDatetime))
    .collect(Collectors.toList());                     // Collect

// Find first matching element
Optional<Vehicle> availableVehicle = vehicles.stream()
    .filter(v -> "available".equals(v.getStatus()))
    .findFirst();

// Group by status
Map<String, List<Booking>> bookingsByStatus = bookings.stream()
    .collect(Collectors.groupingBy(Booking::getStatus));

// Calculate sum
BigDecimal totalRevenue = invoices.stream()
    .map(InvoiceHeader::getTotalAmount)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Parallel stream for large datasets
List<String> results = largeList.parallelStream()
    .filter(this::expensiveOperation)
    .collect(Collectors.toList());
```

### 2. Optional (Null Safety)

```java
// Traditional null check (BAD)
Booking booking = bookingRepository.findById(id);
if (booking == null) {
    throw new NotFoundException();
}

// Using Optional (GOOD)
Booking booking = bookingRepository.findById(id)
    .orElseThrow(() -> new BookingNotFoundException(id));

// Safe access with map
String customerName = Optional.ofNullable(booking)
    .map(Booking::getCustomerDetail)
    .map(BookingCustomerDetail::getFirstName)
    .orElse("Unknown");

// Conditional execution
bookingRepository.findById(id)
    .ifPresent(b -> emailService.sendConfirmation(b));

// Chain operations
Optional<BigDecimal> totalAmount = bookingRepository.findById(id)
    .flatMap(b -> invoiceRepository.findByBookingId(b.getBookingId()))
    .map(InvoiceHeader::getTotalAmount);
```

### 3. Lambda Expressions

```java
// Before Lambda (Anonymous Inner Class)
Comparator<Booking> comparator = new Comparator<Booking>() {
    @Override
    public int compare(Booking b1, Booking b2) {
        return b1.getPickupDatetime().compareTo(b2.getPickupDatetime());
    }
};

// With Lambda
Comparator<Booking> comparator = (b1, b2) -> 
    b1.getPickupDatetime().compareTo(b2.getPickupDatetime());

// Method Reference (even cleaner)
Comparator<Booking> comparator = 
    Comparator.comparing(Booking::getPickupDatetime);

// Functional Interfaces
@FunctionalInterface
public interface BookingValidator {
    boolean validate(Booking booking);
}

BookingValidator statusCheck = b -> "reserved".equals(b.getStatus());
```

### 4. Functional Interfaces

```java
// Built-in functional interfaces
Function<Booking, BookingResponseDTO> mapper = this::buildBookingResponse;
Predicate<Vehicle> isAvailable = v -> "available".equals(v.getStatus());
Consumer<Booking> sendEmail = b -> emailService.sendConfirmation(b);
Supplier<List<Vehicle>> getVehicles = () -> vehicleRepository.findAll();

// Usage in our code
public List<BookingResponseDTO> processBookings(List<Booking> bookings,
        Predicate<Booking> filter,
        Function<Booking, BookingResponseDTO> mapper) {
    
    return bookings.stream()
        .filter(filter)
        .map(mapper)
        .collect(Collectors.toList());
}

// Call with different strategies
List<BookingResponseDTO> active = processBookings(bookings,
    b -> "active".equals(b.getStatus()),
    this::buildBookingResponse);
```

### 5. Java Time API (java.time)

```java
// Instant - Timestamp (UTC)
Instant now = Instant.now();
booking.setCreatedAt(now);

// LocalDate - Date without time
LocalDate pickupDate = LocalDate.of(2024, 2, 15);

// LocalDateTime - Date + Time
LocalDateTime pickupDateTime = LocalDateTime.now();

// Calculate duration
Instant pickup = booking.getPickupDatetime();
Instant returnDate = booking.getReturnDatetime();
long totalDays = ChronoUnit.DAYS.between(
    pickup.atZone(ZoneId.systemDefault()).toLocalDate(),
    returnDate.atZone(ZoneId.systemDefault()).toLocalDate()
);

// Format dates
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
String formatted = LocalDateTime.now().format(formatter);
```

### 6. Multithreading & Concurrency

```java
// Thread creation
Thread emailThread = new Thread(() -> {
    emailService.sendBookingConfirmation(booking);
});
emailThread.start();

// ExecutorService (Thread Pool)
ExecutorService executor = Executors.newFixedThreadPool(10);
Future<Void> future = executor.submit(() -> {
    pdfService.generateInvoice(invoice);
    return null;
});

// CompletableFuture (Async operations)
CompletableFuture.runAsync(() -> emailService.sendConfirmation(booking))
    .thenRunAsync(() -> logger.info("Email sent"))
    .exceptionally(e -> {
        logger.error("Failed to send email", e);
        return null;
    });

// Spring's @Async
@Async
public CompletableFuture<Void> sendEmailAsync(Booking booking) {
    emailService.send(booking);
    return CompletableFuture.completedFuture(null);
}

// Synchronized for thread safety
public synchronized void updateVehicleStatus(Vehicle vehicle, String status) {
    vehicle.setStatus(status);
    vehicleRepository.save(vehicle);
}
```

### 7. Records (Java 14+)

```java
// Traditional DTO (verbose)
public class BookingDTO {
    private final Long id;
    private final String status;
    
    public BookingDTO(Long id, String status) {
        this.id = id;
        this.status = status;
    }
    
    public Long getId() { return id; }
    public String getStatus() { return status; }
    
    @Override
    public boolean equals(Object o) { /* ... */ }
    @Override
    public int hashCode() { /* ... */ }
    @Override
    public String toString() { /* ... */ }
}

// Record (concise) - Java 14+
public record BookingDTO(Long id, String status) {}
// Automatically generates constructor, getters, equals, hashCode, toString
```

### 8. Sealed Classes (Java 17)

```java
// Restrict which classes can extend
public sealed class PaymentStatus permits 
    PendingStatus, SuccessStatus, FailedStatus {
    protected final String code;
    public PaymentStatus(String code) { this.code = code; }
}

public final class PendingStatus extends PaymentStatus {
    public PendingStatus() { super("PENDING"); }
}

public final class SuccessStatus extends PaymentStatus {
    public SuccessStatus() { super("SUCCESS"); }
}

public final class FailedStatus extends PaymentStatus {
    public FailedStatus() { super("FAILED"); }
}
```

---

## 🌱 Spring Boot Concepts

### 1. Dependency Injection (IoC)

```java
// Constructor Injection (Recommended)
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    
    @Autowired  // Optional in single-constructor classes
    public BookingService(BookingRepository bookingRepository,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }
}

// Field Injection (Not recommended for testing)
@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
}

// Why Constructor Injection is better:
// 1. Immutable dependencies (final fields)
// 2. Easier to test (pass mocks in constructor)
// 3. Fails fast if dependency missing
```

### 2. Spring Data JPA

```java
// Entity
@Entity
@Table(name = "bookings")
@Data  // Lombok generates getters, setters, toString, equals, hashCode
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAuth userAuth;
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private BookingCustomerDetail customerDetail;
    
    @Column(name = "status")
    private String status;
}

// Repository - JPA provides implementations automatically
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Derived Query Methods
    List<Booking> findByStatus(String status);
    List<Booking> findByUserAuth_UserId(Long userId);
    Optional<Booking> findByBookingIdAndStatus(Long id, String status);
    
    // Custom JPQL Query
    @Query("SELECT b FROM Booking b WHERE b.pickupHub.hubId = :hubId")
    List<Booking> findByHubId(@Param("hubId") Long hubId);
    
    // Native Query
    @Query(value = "SELECT * FROM bookings WHERE status = ?1", nativeQuery = true)
    List<Booking> findByStatusNative(String status);
}
```

### 3. @Transactional

```java
@Service
public class BookingService {
    
    @Transactional  // All or nothing
    public ApiResponseDTO<BookingResponseDTO> createBooking(BookingRequestDTO request) {
        // Create booking
        Booking booking = new Booking();
        booking.setStatus("reserved");
        bookingRepository.save(booking);  // SQL INSERT
        
        // Create customer detail
        BookingCustomerDetail detail = new BookingCustomerDetail();
        detail.setBooking(booking);
        customerDetailRepository.save(detail);  // SQL INSERT
        
        // If exception here, BOTH inserts are rolled back!
        emailService.sendConfirmation(booking);
        
        return ApiResponseDTO.success("Created", buildResponse(booking));
    }
    
    @Transactional(readOnly = true)  // Optimization for reads
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(this::buildResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logActivity(Long bookingId, String action) {
        // Runs in separate transaction
    }
}
```

### 4. Spring Security + JWT

```java
// Security Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/staff/**").hasAnyRole("ADMIN", "STAFF")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
}

// JWT Token Generation
public String generateToken(UserAuth user) {
    return Jwts.builder()
        .setSubject(user.getEmail())
        .claim("userId", user.getUserId())
        .claim("role", user.getRole())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
}
```

### 5. AOP (Aspect-Oriented Programming)

```java
@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    // Before any method in service package
    @Before("execution(* com.example.service.*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Entering: {}.{}()",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName());
    }
    
    // After returning successfully
    @AfterReturning(pointcut = "execution(* com.example.service.*.*(..))",
                    returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        logger.info("Exiting: {}.{}() with result: {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            result);
    }
    
    // Around - Full control
    @Around("@annotation(Timed)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        logger.info("{} executed in {} ms", joinPoint.getSignature(), duration);
        return result;
    }
}
```

---

## 🔐 Authentication & Authorization

### JWT Authentication Flow

```
┌─────────────────┐
│  User Login     │
│  (email + pass) │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Validate with   │
│ BCrypt hash     │
└────────┬────────┘
         │ Valid
         ▼
┌─────────────────┐
│ Generate JWT    │
│ with claims     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Client stores   │
│ token           │
└─────────────────┘

Subsequent Requests:
┌─────────────────┐
│ Request with    │
│ Authorization   │
│ Bearer {token}  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ JWT Filter      │
│ validates token │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Set Security    │
│ Context         │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Controller      │
│ handles request │
└─────────────────┘
```

---

## 📊 Business Logic & Flows

### Complete Booking Lifecycle

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         FLEMAN BOOKING LIFECYCLE                             │
└─────────────────────────────────────────────────────────────────────────────┘

STEP 1: CREATE BOOKING (Customer)
══════════════════════════════════
POST /api/bookings

    ┌─────────────────────────────────┐
    │  BookingService.createBooking() │
    └─────────────────────────────────┘
              │
              ├── Validate request (dates, vehicle type, hubs)
              ├── Create Booking record (status: "reserved")
              ├── Create BookingCustomerDetail record
              └── 📧 Send confirmation email
              │
              ▼
        Booking Status: RESERVED
        Vehicle Status: (unchanged - not assigned yet)


STEP 2: VEHICLE HANDOVER / PICKUP (Staff at Hub)
════════════════════════════════════════════════
POST /api/handovers

    ┌─────────────────────────────────────┐
    │  HandoverService.createHandover()   │
    └─────────────────────────────────────┘
              │
              ├── Validate booking status == "reserved"
              ├── Assign specific vehicle to booking
              ├── Create Handover record (timestamp, fuel status)
              ├── Update Booking status → "active"
              └── Update Vehicle status → "rented"
              │
              ▼
        Booking Status: ACTIVE
        Vehicle Status: RENTED


STEP 3: VEHICLE RETURN & INVOICE (Staff at Hub)
════════════════════════════════════════════════
POST /api/invoices/return

    ┌───────────────────────────────────────────┐
    │  InvoiceService.processVehicleReturn()    │
    └───────────────────────────────────────────┘
              │
              ├── Validate booking status == "active"
              ├── Calculate rental days (handover → return)
              ├── 💰 Smart Pricing Calculation:
              │       └── Months (30 days) + Weeks (7 days) + Days
              ├── Calculate addon charges
              ├── Apply discount offers (if active at pickup)
              ├── Create InvoiceHeader record (payment: "pending")
              ├── Update Booking status → "returned"
              ├── 🚗 Update Vehicle hub → Return Hub Location
              ├── Update Vehicle status → "available"
              ├── 📄 Generate PDF invoice
              └── 📧 Send invoice email to customer
              │
              ▼
        Booking Status: RETURNED
        Vehicle Status: AVAILABLE
        Vehicle Hub: RETURN LOCATION ← Important for inter-city transfers!


STEP 4: PAYMENT (Customer Online)
═════════════════════════════════
POST /api/payments/create-order  →  Create Razorpay order
POST /api/payments/verify        →  Verify payment signature

    ┌───────────────────────────────────────┐
    │  PaymentService.verifyPayment()       │
    └───────────────────────────────────────┘
              │
              ├── Verify HMAC-SHA256 signature
              ├── Update Invoice payment status → "success"
              ├── Store Razorpay payment ID
              └── Update Booking status → "completed"
              │
              ▼
        Booking Status: COMPLETED ✓
        Invoice Payment: SUCCESS


═══════════════════════════════════════════════════════════════════════════════

                          STATUS STATE MACHINE

    ┌───────────┐     Handover      ┌───────────┐
    │ RESERVED  │  ───────────────► │  ACTIVE   │
    └───────────┘                   └───────────┘
         │                               │
         │ Cancel                        │ Return
         ▼                               ▼
    ┌───────────┐                   ┌───────────┐
    │ CANCELLED │                   │ RETURNED  │
    └───────────┘                   └───────────┘
                                         │
                                         │ Payment Success
                                         ▼
                                    ┌───────────┐
                                    │ COMPLETED │
                                    └───────────┘

═══════════════════════════════════════════════════════════════════════════════

                    VEHICLE LOCATION TRANSFER EXAMPLE

    Pickup Hub: Mumbai Central
    Return Hub: Nagpur Station

    1. At Handover: Vehicle "MH12AB1234" is at Mumbai Central
    2. Customer uses vehicle...
    3. At Return: Vehicle returned at Nagpur Station
    4. System updates: vehicle.setHub(returnHub)
    5. Result: Vehicle "MH12AB1234" is now available at Nagpur Station!

    This enables inter-city one-way rentals.
```

### Smart Rental Pricing Algorithm

```java
// Greedy algorithm - prioritize cheaper rates for longer periods
private RentalCalculation calculateSmartRental(long totalDays,
        BigDecimal dailyRate, BigDecimal weeklyRate, BigDecimal monthlyRate) {
    
    RentalCalculation calc = new RentalCalculation();
    long remainingDays = totalDays;
    
    // Step 1: Calculate months (30 days)
    if (remainingDays >= 30 && monthlyRate.compareTo(BigDecimal.ZERO) > 0) {
        calc.months = remainingDays / 30;
        remainingDays = remainingDays % 30;
        calc.monthlyAmount = monthlyRate.multiply(BigDecimal.valueOf(calc.months));
    }
    
    // Step 2: Calculate weeks (7 days)
    if (remainingDays >= 7 && weeklyRate.compareTo(BigDecimal.ZERO) > 0) {
        calc.weeks = remainingDays / 7;
        remainingDays = remainingDays % 7;
        calc.weeklyAmount = weeklyRate.multiply(BigDecimal.valueOf(calc.weeks));
    }
    
    // Step 3: Remaining days
    if (remainingDays > 0 && dailyRate.compareTo(BigDecimal.ZERO) > 0) {
        calc.days = remainingDays;
        calc.dailyAmount = dailyRate.multiply(BigDecimal.valueOf(calc.days));
    }
    
    calc.totalRentalAmount = calc.monthlyAmount
        .add(calc.weeklyAmount)
        .add(calc.dailyAmount);
    
    return calc;
}

/*
 * Example: 45 days rental
 * Daily: ₹500, Weekly: ₹2800, Monthly: ₹10000
 *
 * Naive: 45 × ₹500 = ₹22,500
 * Smart: 1 month (₹10,000) + 2 weeks (₹5,600) + 1 day (₹500) = ₹16,100
 * Savings: ₹6,400 (28%)
 */
```

---

## 💳 Payment Integration (Razorpay)

```java
@Service
public class PaymentService {
    
    @Value("${razorpay.key.id}")
    private String keyId;
    
    @Value("${razorpay.key.secret}")
    private String keySecret;
    
    public Map<String, Object> createOrder(Long invoiceId) throws RazorpayException {
        InvoiceHeader invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new NotFoundException("Invoice not found"));
        
        RazorpayClient client = new RazorpayClient(keyId, keySecret);
        
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", invoice.getTotalAmount()
            .multiply(BigDecimal.valueOf(100)).intValue());  // Paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "invoice_" + invoiceId);
        
        Order order = client.orders.create(orderRequest);
        
        invoice.setRazorpayOrderId(order.get("id"));
        invoiceRepository.save(invoice);
        
        return Map.of(
            "orderId", order.get("id"),
            "amount", order.get("amount"),
            "currency", order.get("currency")
        );
    }
    
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        // HMAC-SHA256 verification
        String data = orderId + "|" + paymentId;
        String generatedSignature = HmacUtils.hmacSha256Hex(keySecret, data);
        return generatedSignature.equals(signature);
    }
}
```

---

## 🎓 Interview Preparation

### Core Java Questions

#### Q1: Explain the difference between == and .equals()

```
A: == compares references (memory addresses)
   .equals() compares content (can be overridden)

   String s1 = new String("hello");
   String s2 = new String("hello");
   
   s1 == s2      // false (different objects)
   s1.equals(s2) // true (same content)
```

#### Q2: What is the difference between ArrayList and LinkedList?

```
A: ArrayList:
   - Backed by array
   - Fast random access O(1)
   - Slow insert/delete in middle O(n)
   - Good for: Reading, iteration

   LinkedList:
   - Backed by doubly-linked nodes
   - Slow random access O(n)
   - Fast insert/delete O(1)
   - Good for: Frequent add/remove operations
```

#### Q3: Explain HashMap internal working

```
A: 1. hashCode() called on key
   2. Hash determines bucket index
   3. If collision → linked list (or tree for 8+ elements in Java 8)
   4. equals() used to find exact key in bucket
   
   Time Complexity:
   - Put/Get: O(1) average, O(n) worst case
   - Java 8+: O(log n) worst case (tree)
```

#### Q4: What is the difference between final, finally, and finalize?

```
A: final:
   - Variable: Cannot be reassigned
   - Method: Cannot be overridden
   - Class: Cannot be extended

   finally:
   - Block that always executes after try-catch
   - Used for cleanup (closing resources)

   finalize:
   - Method called by GC before object destruction
   - Deprecated in Java 9 (use try-with-resources)
```

#### Q5: Explain String immutability

```
A: Once created, String value cannot change.

   String s = "hello";
   s.concat(" world");  // Creates NEW string
   System.out.println(s);  // Still "hello"
   
   Benefits:
   - Thread-safe (no synchronization needed)
   - String pool optimization
   - Security (can't be modified)
   - Hash code caching
```

### Spring Boot Questions

#### Q6: Explain @Autowired and Dependency Injection

```
A: DI is a design pattern where dependencies are provided externally
   rather than created internally.

   @Autowired tells Spring to inject the dependency:
   
   @Service
   public class BookingService {
       @Autowired
       private BookingRepository repository;  // Spring injects this
   }
   
   Benefits:
   - Loose coupling
   - Easy testing (inject mocks)
   - Centralized configuration
```

#### Q7: What is the difference between @Component, @Service, @Repository?

```
A: All are stereotypes for Spring-managed beans:

   @Component - Generic component
   @Service - Business logic layer (semantic)
   @Repository - Data access layer (adds exception translation)
   @Controller - Web layer (handles HTTP requests)
   
   Functionally similar, but semantically different.
   @Repository also adds DataAccessException translation.
```

#### Q8: Explain @Transactional propagation

```
A: Propagation defines transaction boundaries:

   REQUIRED (default): Join existing or create new
   REQUIRES_NEW: Always create new (suspend existing)
   NESTED: Create nested transaction within existing
   SUPPORTS: Use existing, or run without transaction
   NOT_SUPPORTED: Suspend existing, run without transaction
   NEVER: Throw exception if transaction exists
   MANDATORY: Throw exception if no transaction exists
```

#### Q9: What is Spring AOP?

```
A: Aspect-Oriented Programming separates cross-cutting concerns:

   Cross-cutting: Logging, security, transactions, caching
   
   Terms:
   - Aspect: Module containing cross-cutting logic
   - Advice: Action taken (before, after, around)
   - Pointcut: Expression matching join points
   - Join Point: Method execution (where advice applies)
   
   We use AOP for:
   - Method execution logging
   - Performance timing
   - Transaction management
```

#### Q10: Explain JPA N+1 Problem and Solution

```
A: Problem: Loading entity and then each relationship separately
   
   // N+1 queries (1 for bookings + N for each customer)
   List<Booking> bookings = bookingRepository.findAll();
   for (Booking b : bookings) {
       b.getCustomerDetail().getFirstName();  // Extra query!
   }
   
   Solutions:
   1. Eager fetching: @ManyToOne(fetch = FetchType.EAGER)
   2. JOIN FETCH query:
      @Query("SELECT b FROM Booking b JOIN FETCH b.customerDetail")
   3. Entity Graph:
      @EntityGraph(attributePaths = {"customerDetail"})
```

### Advanced Questions

#### Q11: How does your smart pricing algorithm work?

```
A: Greedy approach prioritizing cheaper unit rates:

   1. Fill maximum complete months (30 days)
   2. Fill maximum complete weeks (7 days) from remainder
   3. Charge remaining days at daily rate
   
   Time: O(1) - just division operations
   Space: O(1) - fixed variables
   
   Optimal because: Monthly rate/30 < Weekly rate/7 < Daily rate
```

#### Q12: How do you handle concurrent booking requests?

```
A: Multiple strategies:

   1. Optimistic Locking (@Version):
      @Version
      private Long version;
      
      Throws OptimisticLockException if version mismatch
   
   2. Pessimistic Locking:
      @Lock(LockModeType.PESSIMISTIC_WRITE)
      Optional<Vehicle> findByIdForUpdate(Long id);
   
   3. Database constraints:
      Unique constraint on vehicle + active booking
   
   4. Transaction isolation:
      @Transactional(isolation = Isolation.SERIALIZABLE)
```

#### Q13: Explain your microservice communication

```
A: We use REST-based synchronous communication:

   Java Backend → .NET Microservice (for file uploads)
   
   RestTemplate restTemplate = new RestTemplate();
   ResponseEntity<String> response = restTemplate.postForEntity(
       microserviceUrl + "/api/hubs/upload",
       multipartRequest,
       String.class
   );
   
   Why microservice:
   - File processing is CPU-intensive
   - Can scale independently
   - Failure isolation
   - Both Java and .NET backends use same service
```

#### Q14: How do you secure your payment flow?

```
A: Multiple security layers:

   1. HMAC-SHA256 signature verification
   2. Server-side order creation (amount stored on server)
   3. Never trust client-provided amounts
   4. Idempotency checks (already paid?)
   5. HTTPS for all communication
   6. Secrets in environment variables
   7. Audit logging of all transactions
```

---

## 🚀 Running the Application

### Prerequisites

- Java 17
- Maven 3.8+
- MySQL 8.0

### Steps

```bash
# 1. Start MySQL and create database
mysql -u root -p
CREATE DATABASE fleeman;

# 2. Configure application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/fleeman
spring.datasource.username=root
spring.datasource.password=root

# 3. Build and run
cd d:\new cdac project\IdeaProjects\demo
mvn clean install
mvn spring-boot:run

# 4. Access application
open http://localhost:8080
```

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Baeldung Spring Tutorials](https://www.baeldung.com/)
- [Java 17 Features](https://openjdk.org/projects/jdk/17/)

---

**Version**: 1.0.0  
**Last Updated**: February 2026  
**Author**: CDAC Project Team
