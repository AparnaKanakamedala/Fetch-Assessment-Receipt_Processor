# Fetch-Receipts-Processor

This project is a Spring Boot application for processing receipts, packaged and deployed using Docker.

## Built With

- **Java**: Primary programming language
- **Spring Boot**: Backend framework for building the REST API
- **Docker**: Containerization platform for packaging the application
- **Gradle**: Build automation tool

## Prerequisites

- **Docker**

Ensure Docker is running on your machine before attempting to build and run the application container.

## Building the Project

To build the application, use the following command:

         docker build --build-arg JAR_FILE=JarFile/ReceiptProcessor-0.0.1-SNAPSHOT.jar -t receiptprocessor-0.0.1 .

## Running the Project

To run the application, use the following command:

        docker run -p 8080:8080 receiptprocessor-0.0.1

The application will run on http://localhost:8080.

POST : http://localhost:8080/reciepts/process
GET : http://localhost:8080/reciepts/{id}/points
