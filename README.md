# crud-backend-spring

This project demonstrates the implementation of security using Spring Boot 3.0 and JSON Web Tokens (JWT).

## Features

- A PostgreSQL DOCKER container is used for persistence.
- User registration and login with JWT authentication
- Request Logging is configured using logback.

# Getting Started
It includes the following features:
The application runs on http://localhost:8081. You can try the following in browser.

```
http://localhost:8081/v1/api/users
```

or using cURL to add an user

```
curl --location --request POST 'http://localhost:8081/v1/api/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "John",
    "lastName": "Smith",
    "gender": "male",
    "email": "jSmith@gmail.com"
}'

```

Run `docker compose -f ./docker-compose.yml up -d` in project folder to start postgres.


## Spring Security 

To add security to the CRUD app, first add following dependency in pom.xml

```
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```

Then, we want to make `SystemUser` class implements Spring Security's `UserDetails`