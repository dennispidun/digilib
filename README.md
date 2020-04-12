# Digitale Bibliotheksverwaltung

## Requirements

For building and running the application you need:

- [JDK 11.0.5](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [npm 6.9.0](https://nodejs.org/en/download/)
- Angular 8 (```cd client && npm install```)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method 
in the `de.unihildesheim.digilib.Application` class from your IDE.

Alternatively you can use the [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins.html#build-tool-plugins-gradle-plugin) like so:

```shell
$ ./gradlew bootRun
```

## Compiling Angular

In order to output the Angular application correctly from the web server, it must be compiled correctly. To ensure this, you must first run:

```shell
$ ./gradlew copy
```