# GitHubRepositoryTestTaskApplication
This project allows you to get all GitHub user repositories that are not forks

## Description
This project is a solution for a recruitment task. It is a Spring Boot application that exposes a REST API to list a GitHub user's non-forked repositories along with their branches and the last commit SHA for each branch.

## Features
 Fetches public repositories for a specified GitHub user.
Filters out forked repositories.
For each non-forked repository, it provides:
    -   Repository Name
    -   Owner Login (`userName`)
    -   A list of all branches with their name and last commit SHA.
Handles non-existent GitHub users by returning a `404 Not Found` error.

## API Documentation

### Get User Repositories

This endpoint retrieves repository information for a given GitHub username.

`GET /api/repos/{username}`

**Path Variable:**
-   `username` (string, required): The GitHub username

---

#### Success Response (200 OK)
-   **Content-Type:** `application/json`
-   **Body:** An array of repository objects.

**Example Request:**
```bash
curl --location 'http://localhost:8080/api/repos/gigaChad'
```

**Example Response Body:**
```json
[
    {
        "repositoryName": "Linux",
        "ownerLogin": "gigaChad",
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "7f5d1a60b01f91c314f59955h4e4d4e80d8edf11d"
            }
        ]
    },
    {
        "repositoryName": "Ubuntu",
        "ownerLogin": "gigaChad",
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "d0dd1f61b33d64e29d8bc1372a94ef7a2135c524"
            },
            {
                "name": "dev",
                "lastCommitSha": "b3cbk597c62b212f4c5225fm8c5391c7849c4114"
            }
        ]
    }
]
```

---

#### Error Response (404 Not Found)

This response is returned when the specified GitHub user does not exist.

**Example Request (non-existent user):**
```bash
curl --location 'http://localhost:8080/api/repos/NotFoundUser'
```

**Example Response Body:**
```json
{
    "status": 404,
    "message": "GitHub user 'NotFoundUser' not found"
}
```

---
**Note on GitHub API Rate Limiting**

The application communicates with the public GitHub API. Unauthenticated requests are subject to a rate limit (typically 60 requests per hour per IP address). If you encounter `403 Forbidden` errors during testing, you have likely exceeded this limit.

## Tech Stack
-   **Java 21**
-   **Spring Boot 3.5.3**
-   **Maven**
-   **RestClient** for consuming external REST APIs.

## Prerequisites
-   Java JDK 21 or newer.
-   Apache Maven.

## How to Get Started

1.  **Clone the repository and navigate into the project directory.**

2.  **Build and Run the application:**
    The project includes a Maven wrapper, which is the recommended way to build and run it.

    **On macOS/Linux:**
    ```bash
    ./mvnw spring-boot:run
    ```
    **On Windows:**
    ```bash
    mvnw.cmd spring-boot:run
    ```
    The application will start and be accessible at `http://localhost:8080`.

## How to Run Tests
The project contains a single integration test that covers the main business "happy path," as required by the task specifications.

To execute the tests:

**On macOS/Linux:**
```bash
./mvnw test
```
**On Windows:**
```bash
mvnw.cmd test
```
