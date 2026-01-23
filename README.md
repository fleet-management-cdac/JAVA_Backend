# FLEMAN Backend API Documentation

Fleet Management System - Backend API built with **Spring Boot 3.x**

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Database Schema](#database-schema)
5. [API Endpoints](#api-endpoints)
6. [Booking Flow](#booking-flow)
7. [Smart Pricing](#smart-pricing)
8. [Email Notifications](#email-notifications)
9. [Configuration Files](#configuration-files)

---

## Project Overview

The FLEMAN Backend provides REST APIs for a fleet management system that allows:
- User registration and authentication (JWT)
- Vehicle catalog browsing with rental rates
- User profile management

### Key Features
- **JWT Authentication** - Secure token-based authentication
- **BCrypt Password Hashing** - Secure password storage
- **JPA/Hibernate** - ORM for database operations
- **MySQL Database** - Persistent data storage

---

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming Language |
| Spring Boot | 3.2.x | Framework |
| Spring Security | 6.x | Security & JWT |
| Spring Data JPA | 3.x | Database ORM |
| MySQL | 8.x | Database |
| jjwt | 0.12.3 | JWT Token Handling |
| BCrypt | - | Password Hashing |

---

## Project Structure

```
src/main/java/com/example/
├── config/
│   └── SecurityConfig.java      # Spring Security + BCrypt password encoder
├── controller/
│   ├── AuthController.java      # Login & Registration endpoints
│   ├── UserController.java      # User profile endpoints
│   └── VehicleController.java   # Vehicle catalog endpoints
├── dto/
│   ├── ApiResponseDTO.java      # Standard API response wrapper
│   ├── RegisterRequestDTO.java  # Registration request body
│   ├── LoginRequestDTO.java     # Login request body
│   ├── LoginResponseDTO.java    # Login response with JWT
│   ├── UserResponseDTO.java     # Basic user info response
│   ├── UserProfileDTO.java      # Full user profile response
│   └── VehicleDisplayDTO.java   # Vehicle with rates response
├── entity/
│   ├── UserAuth.java            # user_auth table entity
│   ├── UserDetail.java          # user_details table entity
│   ├── CityMaster.java          # city_master table entity
│   ├── StateMaster.java         # state_master table entity
│   ├── VehicleType.java         # vehicle_types table entity
│   └── VehicleRate.java         # vehicle_rates table entity
├── repository/
│   ├── UserAuthRepository.java
│   ├── UserDetailRepository.java
│   ├── CityMasterRepository.java
│   └── VehicleTypeRepository.java
├── service/
│   ├── AuthService.java         # Registration & Login logic
│   ├── UserService.java         # User profile logic
│   └── VehicleService.java      # Vehicle catalog logic
├── util/
│   └── JwtUtil.java             # JWT token generation & validation
└── FleemanApplication.java      # Main application class
```

---

## Database Schema

### Tables Used

| Table | Purpose |
|-------|---------|
| `user_auth` | User authentication (email, password, role) |
| `user_details` | User profile information |
| `city_master` | City lookup table |
| `state_master` | State lookup table |
| `vehicle_types` | Vehicle categories |
| `vehicle_rates` | Rental rates per vehicle type |

### Entity Relationships

```
user_auth (1) ──────< (Many) user_details
                              │
                              └──> city_master ──> state_master

vehicle_types (1) ──────< (Many) vehicle_rates
```

---

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

---

### 1. Authentication Endpoints

#### POST `/api/auth/register`

Register a new user account.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "secret123",
  "firstName": "John",
  "lastName": "Doe",
  "address": "123 Main Street, Apt 4B",
  "cityId": 1,
  "zipcode": "10001",
  "phoneHome": "555-1234",
  "phoneCell": "9876543210",
  "dateOfBirth": "1990-05-15",
  "drivingLicenseNo": "DL123456789",
  "licenseValidTill": "2030-05-15",
  "passportNo": "P123456789",
  "passportValidTill": "2035-01-01",
  "dipNumber": null,
  "dipValidTill": null
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "userId": 1,
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "customer"
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Email already registered",
  "data": null
}
```

---

#### POST `/api/auth/login`

Authenticate user and get JWT token.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "email": "john@example.com",
    "firstName": "John",
    "role": "customer"
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid email or password",
  "data": null
}
```

---

### 2. User Endpoints

#### GET `/api/users/details/{userDetailId}`

Get user profile by user details ID.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User details fetched",
  "data": {
    "userDetailsId": 1,
    "userId": 1,
    "email": "john@example.com",
    "role": "customer",
    "firstName": "John",
    "lastName": "Doe",
    "address": "123 Main Street, Apt 4B",
    "cityName": "Mumbai",
    "stateName": "Maharashtra",
    "zipcode": "10001",
    "phoneHome": "555-1234",
    "phoneCell": "9876543210",
    "dateOfBirth": "1990-05-15",
    "drivingLicenseNo": "DL123456789",
    "licenseValidTill": "2030-05-15",
    "passportNo": "P123456789",
    "passportValidTill": "2035-01-01",
    "dipNumber": null,
    "dipValidTill": null
  }
}
```

---

### 3. Vehicle Endpoints

#### GET `/api/vehicles/types-with-rates`

Get all vehicle types with their rental rates (daily, weekly, monthly).

**Response (200 OK):**
```json
[
  {
    "vehicleTypeId": 1,
    "typeName": "Chevrolet Aveo or similar",
    "description": "Small Cars",
    "imgUrl": "/images/aveo.png",
    "dailyRate": 12.00,
    "weeklyRate": 79.00,
    "monthlyRate": 310.00
  },
  {
    "vehicleTypeId": 2,
    "typeName": "Chevrolet Cobalt or similar",
    "description": "Compact Cars",
    "imgUrl": "/images/cobalt.png",
    "dailyRate": 18.00,
    "weeklyRate": 120.00,
    "monthlyRate": 500.00
  }
]
```

---

### 4. Booking Endpoints

#### POST `/api/bookings`

Create a new booking (saves to both `bookings` and `booking_customer_details` tables).

**Request Body:**
```json
{
  "userId": null,
  "vehicleId": 1,
  "rateId": 1,
  "pickupHubId": 1,
  "returnHubId": 1,
  "pickupDatetime": "2025-01-25T04:30:00Z",
  "returnDatetime": "2025-01-28T04:30:00Z",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "address": "123 Main Street, Mumbai",
  "cityId": 1,
  "zipcode": "400001",
  "phoneCell": "9876543210",
  "drivingLicenseNo": "MH01-2020-1234567",
  "licenseValidTill": "2030-05-15"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "bookingId": 6,
    "vehicleName": "Maruti Suzuki Swift",
    "vehicleRegistration": "MH01AB1234",
    "pickupHub": "Mumbai Central",
    "returnHub": "Mumbai Central",
    "pickupDatetime": "2025-01-25T04:30:00Z",
    "returnDatetime": "2025-01-28T04:30:00Z",
    "status": "reserved",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com"
  }
}
```

> 📧 **Email:** Confirmation email is automatically sent to customer.

---

#### GET `/api/bookings`

Get all bookings.

#### GET `/api/bookings/{bookingId}`

Get booking by ID with all customer details.

#### GET `/api/bookings/user/{userId}`

Get all bookings for a specific user.

#### PATCH `/api/bookings/{bookingId}/status?status=cancelled`

Update booking status. Valid values: `reserved`, `modified`, `cancelled`, `completed`

#### DELETE `/api/bookings/{bookingId}`

Delete a booking.

---

### 5. Handover Endpoints

#### POST `/api/handovers`

Create handover when vehicle is given to customer.

**Request Body:**
```json
{
  "bookingId": 1,
  "processedBy": 2,
  "fuelStatus": "Full"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Handover created successfully",
  "data": {
    "handoverId": 1,
    "bookingId": 1,
    "customerName": "John Doe",
    "vehicleName": "Maruti Suzuki Swift",
    "processedByName": "Staff One",
    "fuelStatus": "Full",
    "createdAt": "2025-01-25T04:30:00Z"
  }
}
```

#### GET `/api/handovers/{handoverId}`

Get handover by ID.

#### GET `/api/handovers/booking/{bookingId}`

Get handovers for a booking.

---

### 6. Invoice Endpoints

#### POST `/api/invoices/return`

Process vehicle return and generate invoice with **smart pricing calculation**.

**Request Body:**
```json
{
  "bookingId": 1,
  "actualReturnDate": "2025-02-04"
}
```

**Response (with pricing breakdown):**
```json
{
  "success": true,
  "message": "Vehicle returned & invoice generated",
  "data": {
    "invoiceId": 1,
    "invoiceDate": "2025-02-04",
    "bookingId": 1,
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "vehicleName": "Maruti Suzuki Swift",
    "vehicleRegistration": "MH01AB1234",
    "handoverDate": "2025-01-25",
    "returnDate": "2025-02-04",
    "totalDays": 10,
    "dailyRate": 1500.00,
    "weeklyRate": 9000.00,
    "monthlyRate": 30000.00,
    "monthsCharged": 0,
    "weeksCharged": 1,
    "daysCharged": 3,
    "monthlyAmount": 0.00,
    "weeklyAmount": 9000.00,
    "dailyAmount": 4500.00,
    "pricingBreakdown": "1 week(s) × ₹9000.00 = ₹9000.00 + 3 day(s) × ₹1500.00 = ₹4500.00",
    "rentalAmount": 13500.00,
    "addonName": "GPS Navigation",
    "addonPricePerDay": 200.00,
    "addonTotalAmount": 2000.00,
    "totalAmount": 15500.00
  }
}
```

#### GET `/api/invoices/{invoiceId}`

Get invoice by ID.

#### GET `/api/invoices/booking/{bookingId}`

Get invoice for a booking.

---

## Complete API Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Auth** | | |
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login & get JWT |
| **Users** | | |
| GET | `/api/users/details/{id}` | Get user profile |
| **Vehicles** | | |
| GET | `/api/vehicles/types-with-rates` | Get vehicle catalog |
| **Bookings** | | |
| POST | `/api/bookings` | Create booking + send email |
| GET | `/api/bookings` | Get all bookings |
| GET | `/api/bookings/{id}` | Get booking by ID |
| GET | `/api/bookings/user/{userId}` | Get user's bookings |
| PATCH | `/api/bookings/{id}/status` | Update status |
| DELETE | `/api/bookings/{id}` | Delete booking |
| **Handovers** | | |
| POST | `/api/handovers` | Create handover |
| GET | `/api/handovers/{id}` | Get handover |
| GET | `/api/handovers/booking/{id}` | Get by booking |
| **Invoices** | | |
| POST | `/api/invoices/return` | Process return & invoice |
| GET | `/api/invoices/{id}` | Get invoice |
| GET | `/api/invoices/booking/{id}` | Get by booking |

---

## Booking Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    FLEMAN BOOKING LIFECYCLE                     │
└─────────────────────────────────────────────────────────────────┘

 STEP 1: CREATE BOOKING
 ──────────────────────
 POST /api/bookings
       │
       ├── Save to bookings table
       ├── Save to booking_customer_details table
       ├── Update vehicle status → "rented"
       └── 📧 Send confirmation email

 STEP 2: VEHICLE HANDOVER
 ────────────────────────
 POST /api/handovers
       │
       ├── Record handover timestamp
       └── Store fuel status, processed by staff

 STEP 3: VEHICLE RETURN & INVOICE
 ────────────────────────────────
 POST /api/invoices/return
       │
       ├── Calculate rental days
       ├── Smart pricing (months + weeks + days)
       ├── Calculate addon amount
       ├── Generate invoice
       ├── Update booking status → "completed"
       └── Update vehicle status → "available"
```

---

## Smart Pricing

The invoice calculates optimal pricing based on duration:

```
For X days:
├── Months = X ÷ 30 (full months)
├── Weeks = Remaining ÷ 7 (full weeks)
└── Days = Remaining (leftover days)

Total = (Months × Monthly Rate) + (Weeks × Weekly Rate) + (Days × Daily Rate)
```

**Examples:**

| Days | Calculation | Formula |
|------|-------------|---------|
| 5 | 5 daily | 5 × ₹1,500 = ₹7,500 |
| 7 | 1 week | 1 × ₹9,000 = ₹9,000 |
| 10 | 1 week + 3 days | ₹9,000 + 3×₹1,500 = ₹13,500 |
| 35 | 1 month + 5 days | ₹30,000 + 5×₹1,500 = ₹37,500 |
| 45 | 1 month + 2 weeks + 1 day | ₹30,000 + 2×₹9,000 + ₹1,500 = ₹49,500 |

---

## Email Notifications

| Event | Email Sent To | Content |
|-------|---------------|---------|
| **Booking Created** | Customer email | Booking ID, vehicle, dates, hubs, customer info |
| **Invoice Generated** | Customer email | Invoice with PDF attachment (optional) |

**Email Configuration (application.properties):**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## Configuration Files

### application.properties

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/fleman
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your-super-secret-key-256-bits
jwt.expiration=86400000

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

---

## Error Handling

All errors return standard format:

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

| Status | Meaning |
|--------|---------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 404 | Not Found |

---

## Testing with cURL

### Create Booking
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "rateId": 1,
    "pickupHubId": 1,
    "returnHubId": 1,
    "pickupDatetime": "2025-01-25T04:30:00Z",
    "returnDatetime": "2025-01-28T04:30:00Z",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "phoneCell": "9876543210",
    "drivingLicenseNo": "MH01-2020-1234567"
  }'
```

### Create Handover
```bash
curl -X POST http://localhost:8080/api/handovers \
  -H "Content-Type: application/json" \
  -d '{"bookingId": 1, "processedBy": 2, "fuelStatus": "Full"}'
```

### Process Return & Invoice
```bash
curl -X POST http://localhost:8080/api/invoices/return \
  -H "Content-Type: application/json" \
  -d '{"bookingId": 1, "actualReturnDate": "2025-01-28"}'
```

---

## Hub Master Excel Upload

### Endpoint

**POST** `/api/locations/upload`

Upload hub/location data in bulk using an Excel file.

---

### Excel File Format

| Column A | Column B | Column C | Column D |
|----------|----------|----------|----------|
| Hub Name | Hub Address | City ID | State ID |
| Mumbai Central | 123 Main Road, Mumbai | 1 | 1 |
| Pune Station | 45 Station Road, Pune | 2 | 1 |

**File Requirements:**
- Format: `.xlsx` (Excel Workbook)
- Data starts from Row 1 (Header) and Column A
- City ID and State ID must exist in database

---

### Postman Testing

1. **Method:** POST
2. **URL:** `http://localhost:8080/api/locations/upload`
3. **Body:** Select `form-data`
   - Key: `file` (change dropdown to **File**)
   - Value: Select your `hubs.xlsx` file
4. **Send**

---

### Backend Logic

```
Excel Upload → Parse Rows → Check if Hub Exists
                                    │
                    ┌───────────────┴───────────────┐
                    ↓                               ↓
              Hub Exists?                      Not Found?
                    ↓                               ↓
              UPDATE Hub                      CREATE New Hub
                    │                               │
                    └───────────────┬───────────────┘
                                    ↓
                            Save to Database
```

**Upsert Logic:** If a hub with the same name and city already exists, it updates the record. Otherwise, it creates a new one.

---

### Response

**Success (200 OK):**
```json
{
  "success": true,
  "message": "Hubs uploaded successfully"
}
```

---

*Documentation for FLEMAN Fleet Management System - Group 08*
