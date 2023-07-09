# ADS Project

## Description
This is a Java + Spring (Spring Boot) + H2 DB application that provides a REST API to manage users and their communication methods.

## Requirements and functionalities
- Method 1: Add a user to the system with basic data (first name, last name, PESEL).
- Method 2: Add communication methods for a user (email, home address, registered address, private phone number, business phone number).
- Method 3: Retrieve all users from the system.
- Method 4: Retrieve a specific user by PESEL.

## Usage
- Use the REST API to interact with the application.
- Access the application via a web browser to view all users: http://localhost:8080/users/all
- Access project DB: http://localhost:8080/h2-console
- Access Swagger documentation: http://localhost:8080/swagger-ui.html

## Unit Tests
- Three unit tests have been implemented to ensure the correctness of the functionality.

## Export Users to File
- A REST method is available to save all users to a file.

## Generating Random Users
- The application can automatically generate 15000 users with 4 communication methods for each user (pseudorandom).

## Estimated Time
The estimated time for completing the project was 5-8 hours.
The total time spent on the project was 15.5 hours 



