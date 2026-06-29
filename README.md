# Booking-System

Booking-System is a small backend portfolio project built with Java and Spring Boot.

The goal of this project is to practice clean backend development step by step: REST API design, layered architecture, validation, error handling, and later database integration with PostgreSQL.

## Current Status

This is version 1 of the project.

At the moment, the application uses an in-memory repository. This means that all bookings are stored only while the application is running. After restarting the application, the initial test data is loaded again.

## Technologies

* Java 17
* Spring Boot
* Maven
* REST API
* In-memory data storage
* Layered architecture

## Project Structure

The project is organized into several modules:

```text
booking-system
├── booking-api
├── booking-application
├── booking-domain
├── booking-infrastructure
└── pom.xml
```

The current API flow is:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
In-memory storage
```

## Main Features

The project currently supports basic booking management:

* Get all bookings
* Get a booking by ID
* Create a new booking
* Cancel an existing booking
* Delete a booking
* Validate input data
* Return custom error responses 
* Simple static web page for displaying bookings

## API Endpoints

### Get all bookings

```http
GET /api/bookings
```

Example:

```text
http://localhost:8080/api/bookings
```

### Get booking by ID

```http
GET /api/bookings/{id}
```

Example:

```text
http://localhost:8080/api/bookings/1
```

### Create booking

```http
POST /api/bookings
```

Example JSON body:

```json
{
  "userId": 1,
  "serviceId": 1,
  "startTime": "2026-06-25T14:00:00",
  "endTime": "2026-06-25T14:30:00"
}
```

### Cancel booking

```http
PUT /api/bookings/{id}/cancel
```

Example:

```text
http://localhost:8080/api/bookings/2/cancel
```

### Delete booking

```http
DELETE /api/bookings/{id}
```

Example:

```text
http://localhost:8080/api/bookings/1
```

## Example PowerShell Requests

### Create a booking

```powershell
$body = @{
    userId = 1
    serviceId = 1
    startTime = "2026-06-25T14:00:00"
    endTime = "2026-06-25T14:30:00"
} | ConvertTo-Json

Invoke-RestMethod `
    -Method Post `
    -Uri "http://localhost:8080/api/bookings" `
    -ContentType "application/json" `
    -Body $body
```

### Cancel a booking

```powershell
Invoke-RestMethod `
    -Method Put `
    -Uri "http://localhost:8080/api/bookings/2/cancel"
```

### Delete a booking

```powershell
Invoke-RestMethod `
    -Method Delete `
    -Uri "http://localhost:8080/api/bookings/1"
```

## How to Run

The application can be started from IntelliJ IDEA by running:

```text
BookingApiApplication
```

After the application starts, open the following URL in the browser:

```text
http://localhost:8080/api/bookings
```

## Validation

When creating a booking, the application validates the input data.

For example, the end time must not be before the start time.

Invalid requests return an error response instead of creating a booking.

## Current Limitations

This version is intentionally simple.

Current limitations:

* No database yet
* Data is lost after application restart
* No authentication or authorization yet
* No user management yet
* No Docker setup yet

## Roadmap

Planned future improvements:

* Add PostgreSQL database
* Add Spring Data JPA
* Add Flyway database migrations
* Add Docker support
* Add authentication and authorization
* Add more tests
* Improve error handling
* Add user and service management

## Purpose of the Project

This project is part of my backend development learning path.

The main focus is not only to build features, but also to understand how a real backend application is structured and improved step by step.
