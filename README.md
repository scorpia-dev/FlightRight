# FlightRight

## About App

This is a "Member" RESTful Web Service with Spring/Spring Boot/Hibernate H2 in memory database

### You can

* Create a new member
 
```
POST - http://localhost:8080/members
```
```JSON
{
    "firstName": "<first name here>",
    "lastName": "last name here",
    "dateOfBirth": "yyyy-mm-dd",
    "postalCode": "SO17 1BJ",
    "picture": "image.png/jpg/jpeg/"
}
```

* Read an existing member
```
*GET - http://localhost:8080/members/{memberid} i.e.  http://localhost:8080/members/1
 ```
* Update an existing member
```
PUT - http://localhost:8080/members/{memberid}
```
```JSON
{
    "firstName": "<first name here>",
    "lastName": "last name here",
    "dateOfBirth": "yyyy-mm-dd",
    "postalCode": "SO17 1BJ",
    "picture": "image.png/jpg/jpeg/"
}
```
* Delete members which are no longer used
```
*DELETE - http://localhost:8080/members/{memberid} i.e.  http://localhost:8080/members/1
```

* List existing members
```
*GET - http://localhost:8080/members
```

### Spec -
* Accepts JSON 
* Response in JSON 
* JDK8 or higher
* Build with Maven
* Data storage: (in memory) database
* Lombok has been used to reduce boilerplate code

### Running -
Run as a Spring Boot App


