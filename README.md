# crud-backend-spring

This project demonstrates the implementation of security using Spring Boot 3.0 and JSON Web Tokens (JWT).

## Features

- A PostgreSQL DOCKER container is used for persistence.
- User registration and login with JWT authentication
- Request Logging is configured using logback.

## Getting Started
It includes the following features:
The application runs on http://localhost:8081. You can try the following in browser.

```
http://localhost:8081/v1/api/users
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

### Registration

`POST http://localhost:8081/api/v1/auth/register`
with body data 

```
{
    "firstname": "John",
    "lastname": "Smith",
    "password": "password",
    "email": "jSmith@gmail.com"
}
```

or use cURL
```
curl --location --request POST 'http://localhost:8081/api/v1/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstname": "John",
    "lastname": "Smith",
    "password": "password",
    "email": "jSmith@gmail.com"
}'
```

Here're a list of things registration does
- create a new SystemUser in database with encoded password
- create a new Token in database with the generated JWT

### Login

`POST http://localhost:8081/api/v1/auth/authenticate`
wuth body data
```
{
    "password": "password",
    "email": "jSmith@gmail.com"
}
```
if login succeeds, you'll get a JWT token in response
```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqU21pdGhAZ21haWwuY29tIiwiaWF0IjoxNjgwMzgyOTgzLCJleHAiOjE2ODAzODQ0MjN9.BtGVYX4Pku8PtAT2Y9zuWOJkFIQhHcTQCUFsfm8FHI4"
}
```

Here's the login process


    - Authenitcation
        - Spring uses ProviderManager as default AuthenticationManager
        - which in turn uses an AuthenticationProvider configured in ApplicationConfig.java to provide the authentication
        - ApplicationConfig also set the following items used by the AuthenticationProvider
            - UserDetailsService
            - PasswordEncoder
        - Spring Authentication Workflow
            - fetch UserDetails from database with username(email) and password passed in login request
                - this utilize the configured `UserDetailsService` (user need to provide the implementation)
                - basically fetches the 'user' data
                - so it's convenient to define SystemUser entity to extend UserDetails
                - Spring caches result, and only fetches database when  
                    - no matched user in cache 
                    - cached details failed in auth.
            - pre authentication checks
                - default implementation `DefaultPreAuthenticationChecks`
                - check userDetails.isAccountNonLocked()
                - check userDetails.isEnabled()
                - check userDetails.isAccountNonExpired()
            - additional Authentication Checks
                - this is where it checks if the password matches
                - default implementation `DaoAuthenticationProvider`
                - use the selected passwordEncoder to match the give password with stored password
            - post authentication checks
                - default implementation `DefaultPostAuthenticationChecks`
                - check isCredentialsNonExpired()
    - When authenticated, fetch User from database
    - Generate JWT token from user info, jwt contains following claims
        - username (email) as subject
        - issued at
        - expiration
    - revoke all previous tokens with this user in database
        - set expired
        - set revoked
    - save new token in database
    - response with jwt

### JWT
    - a JWTAuthFilter was configure into the Spring Security filter chain at App starting
    - all requests go through the filter chain
    - if a JWT can be pulled from request
        - parse JWT with signing secret
        - extract userName (email) from jwt 
        - pull userDetails with userName (email)
        - verify that the a token record with the JWT
            - exists in database
            - `isExpired` is false
            - `revoked` is false
        - verity that username from JWT
            - matches username matches the username in userDetails
                - this seems redundant as userDetails is fetched based on the JWT username
            - check `expiration` claim 
        - create an UsernamePasswordAuthenticationToken, and set in security context
            - SecurityContextHolder.getContext().setAuthentication(authToken)
            - The security context contains information about the current authentication status of a user, including their principal (i.e., username or user ID) and their granted authorities (i.e., roles or permissions).
            - this can be later used by Spring Security to enforce access control rules and authorize user requests to protected resources.


### Password Encoder

 - BCrypt
    - use a random salt to hash password
        - base64 encoded salt has fixed length of 22
        - the salt can be extracted from hashed password 
    - match plain text password with hashed password
        - extract salt from hased password
        - hash the plain text password with the extracted salt
        - compared result with stored hased password 
 - Pbkdf2PasswordEncoder
    - Password-Based Key Derivation Function 2 (PBKDF2)
    - also generates a random salt
        - let user also input a secret
        - combine the salt and user entered secret to hash the password
        - concatenate the salt and hased password as encoded result
    - match plain text password with hashed password
        - extract the salt
        - encode the password in the same way above
        - compare result with stored result


## CRUD

### Find all users
`GET 'http://localhost:8081/api/v1/users`
You need Bearer Authorization token in your header.

```
curl --location --request GET 'http://localhost:8081/api/v1/users' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqU21pdGhAZ21haWwuY29tIiwiaWF0IjoxNjgwMzgyOTgzLCJleHAiOjE2ODAzODQ0MjN9.BtGVYX4Pku8PtAT2Y9zuWOJkFIQhHcTQCUFsfm8FHI4' \
--header 'Cookie: JSESSIONID=5EF865AECF0D7149CF49165374C132E5'
```

### Find user by email



### Update user
```
PUT http://localhost:8081/api/v1/users/{{userId}}
```

### Delete user

```
DELETE http://localhost:8081/api/v1/users/{{userId}}
```
