# crud-backend-spring

This is a simple Spring Boot application that implements CRUD operations on a User model.

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


A PostgreSQL DOCKER container is used for persistence.

Run `docker compose -f ./docker-compose.yml up -d` in project folder to start postgres.

No Spring Security is set up.

Request Logging is configured using logback.


