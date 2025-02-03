# Minesweeper

## About
A RESTful web-service of a minesweeper game. Players can start new game or make turn.

## Stack
Java, Spring Framework, PostgreSQL, JPA(Hibernate), Maven, Docker, Lombok, Liquibase. Junit, Mockito

## Architecture
_______________________________________________________________
Web-service is monolite service.

The interaction of modules is made through RestTemplate;

The service is also ready for use on the Docker platform;
_______________________________________________________________

## Requirements
[Docker](https://www.docker.com/) is required as a minimum for the service to work, Linux system or WSL to run a service in a container.

## Endpoints
- [Endpoints](https://minesweeper-test.studiotg.ru/swagger/)

## Installation
First clone this repository to your local machine:

    git clone

Then, run:

    docker compose up

This command will build new image from the Dockerfile for the app and starts containers with app and database.

## Usage
To play, go [here](https://minesweeper-test.studiotg.ru/) and change the API URL to `http://localhost:8080/minesweeper`
