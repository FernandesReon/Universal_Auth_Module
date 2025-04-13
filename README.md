# Universal Authentication Module

## Overview
UAM is a flexible and reusable authentication module built using Spring Boot designed to integrate into
any project. It ensures that users can safely and securely register, login, and manage their accounts while high-security
standards are maintained. This module includes *Password encryption*, *OAuth 2.0 (Google and GitHub)*, and *Role-based access*
control to manage different user permissions.

---

## Features
* **User Authentication**: Secure login and registration system.
* **OAuth 2.0 Login**: Users can log in with their Google and GitHub.
* **Role-based Access**: Different permission for Admin and Users.
* **Password Security**: Uses encryption to keep password safe.
* **Email Verification**: Users verify their identify for added security.
* **API Rate Limiting**: Prevents too many login attempts using Spring Cloud Gateway.
* **Database Support**: Stores user details in a MySQL database for further reference.

## Tech stack
* **Spring Boot** - A Java-based framework for backend development.
* **Spring Security** - Handles user authentication and permissions.
* **OAuth 2.0** - Allows login using third party accounts.
* **JWT (JSON Web Token)** - Provides a secure way to log users in (yet to implement).
* **MySQL** - Store user information securely.
* **Redis** - Improves performance while performing CRUD operations (High throughput, Low latency).
* **BCrypt** - Encrypts passwords before storing them.


## Getting Started

### Step 1: Clone the Repository
To start using this authentication module, download the project files:
```sh
git clone https://github.com/FernandesReon/Universal_Auth_Module.git
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
spring.data.redis.timeout=60000
````

#### oauth configuration
````
spring.security.oauth2.client.registration.google.client-name=google
spring.security.oauth2.client.registration.google.client-id=
spring.security.oauth2.client.registration.google.client-secret=
spring.security.oauth2.client.registration.google.scope=openid,profile,email
````

#### logging
````
logging.level.root=info
````

### Step 3: Run the Application
```
mvn spring-boot:run
```
Once the application starts, it will be available at *http://localhost:8080/user/*