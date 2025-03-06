# Universal Authentication Module (Spring Boot)

## Overview
A flexible and reusable authentication module built using Spring Boot, designed to be integrated into any project.
It ensures that users can safely sign up, log in, and manage their accounts while maintaining high-security standards.
This module includes *Password encryption*, *OAuth2 authentication (Google & GitHub)*, and *Role-based access control*
to manage different user permissions.

---

## Features
**User Authentication** - Secure login and registration system
**OAuth2 Login** - Users can log in with Google or GitHub
**Role-base Access** - Different permissions for Admin and Users
**Password Security** - Uses encryption to keep password safe
**Email and OTP Verification** - Users verify their identity for added security
**Rate Limiting** - Prevents too many login attempts using Redis
**Database Support** - Stores user details in a MySQL database

## Technologies used
This project is built using industry-standard tools for security and scalability:
- **Spring Boot** – A Java-based framework for backend development
- **Spring Security** – Handles user authentication and permissions
- **JWT (JSON Web Token)** – Provides a secure way to log users in
- **OAuth2** – Allows login using Google and GitHub accounts
- **MySQL** – Stores user information securely
- **Redis** – Improves performance and protects against excessive requests
- **BCrypt** – Encrypts passwords before storing them

## Getting Started

### Step 1: Clone the Repository
To start using this authentication module, download the project files:
```sh
git clone https://github.com/your-username/auth-module.git
cd auth-module
```

### Step 2: Set up the Database
#### mysql database configuration
````
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/databaseName
spring.datasource.username=root
spring.datasource.password=mysqlpassword
````

#### jpa configuration
````
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
````

#### redis configuration
````
spring.data.redis.host=localhost
spring.data.redis.port=6379
````

### Step 3: Run the Application
```
mvn spring-boot:run
```
Once the application starts, it will be available at **http://localhost:8080**