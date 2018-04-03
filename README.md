# user
Example Spring Boot project of REST API for managing users information

## Rest API

The REST API in this project includes:

### `GET /user/`

List all users

#### Example Request

    curl -i http://localhost:8080/user

#### Example Response

    HTTP/1.1 200 
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Tue, 03 Apr 2018 19:30:01 GMT

    [{"id":1,"username":"userOne","email":"joe@something.com","firstname":"Joe","lastname":"Doe"},{"id":2,"username":"userTwo","email":"jane@something.com","firstname":"Jane","lastname":"Smith"}]

### `GET /user/{username}`

Get the information of the user with the specified `username`

#### Example Request

    curl -i http://localhost:8080/user/userOne

#### Example Response

    HTTP/1.1 302 
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Tue, 03 Apr 2018 19:30:42 GMT

    {"id":1,"username":"userOne","email":"joe@something.com","firstname":"Joe","lastname":"Doe"}

### `POST /user/`

Creates a new user. Request schema:

    {
        "username": "string",
        "password": "string",
        "email": "string",
        "firstname": "string (optional)",
        "lastname": "string (optional)"
    }

#### Example Request

    curl -i -H "Content-type: application/json" -d '{"username":"userThree","firstname":"Jill","lastname":"William","email":"jwilliam@something.com","password":"pass3"}' http://localhost:8080/user

#### Example Response

    HTTP/1.1 201 
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Tue, 03 Apr 2018 19:32:06 GMT

    {"id":3,"username":"userThree","email":"jwilliam@something.com","firstname":"Jill","lastname":"William"}

### `PUT /user/{username}`

Updates the information on an existing user with the specified `username`. Request schema:

    {
        "username": "string (optional)",
        "password": "string (optional)",
        "email": "string (optional)",
        "firstname": "string (optional)",
        "lastname": "string (optional)"
    }

#### Example Request

    curl -i -X PUT -H "Content-type: application/json" -d '{"firstname":"The-new-Joe"}' http://localhost:8080/user/userOne

#### Example Response

    HTTP/1.1 200 
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Tue, 03 Apr 2018 19:33:48 GMT

    {"id":1,"username":"userOne","email":"joe@something.com","firstname":"The-new-Joe","lastname":"Doe"}

### `DELETE /user/{username}`

Deletes the user with the specified `username`

#### Example Request

    curl -i -X DELETE http://localhost:8080/user/userTwo

#### Example Response

    HTTP/1.1 204 
    Content-Type: application/json;charset=UTF-8
    Date: Tue, 03 Apr 2018 19:34:47 GMT


## Requirements

* JDK 1.8
* Apache Maven 3.x

The application uses an embedded Tomcat 8 and H2 database so there is no need to install them separately.

## How to run

### As a packaged application

Creates an executable jar

    ./mvnw clean package
     
Run the application

    java -jar target/user-0.0.1-SNAPSHOT.jar


### Using Spring Boot Maven plugin

    mvn spring-boot:run
