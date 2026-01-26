# Spring Boot TaskApp Client

This repository contains the **Spring Boot** implementation of **TaskApp** (server application).

## Purpose

TaskApp is a **learning and experimentation project**.

The same application is intentionally rebuilt using different technologies in order to:
- Learn frameworks and languages
- Compare architectural approaches
- Evaluate developer experience and tooling

This project is **not intended for production use**.

## Application Overview

TaskApp is a simple task management application with authentication and tagging.

### User Flow

1. User creates an account
2. User logs in
3. User creates custom tags
4. User creates tasks associated with tags
5. User views tasks and tags using filters

## Domain Model (Subject to Change)

The client follows the same domain rules as the server.

### User

- Email
- Authentication state (handled via tokens/session from the server)

### Task

Each task contains:
- Title
- Completed status (boolean)
- Overdue date
- Associated tag

### Tag

- Created by the user
- Has a color
- Color options are **fixed and predefined by the application**

## Architecture Role

This repository represents the **server** application of TaskApp.

- Client repositories focus on UI rendering, state management, routing, and user interaction
- Server repositories focus on authentication, business logic, and APIs

The client communicates with the server exclusively through defined API contracts.

## Development Workflow

This project follows the GitHub Flow branching strategy to ensure a stable main branch and a clean, pull-request-driven development process.

## Technology Stack

- Java
- Spring Boot

## Related Repositories

Other implementations of TaskApp using different technologies:
- Client (Angular): Available

## Notes

The UI behavior, domain rules, and API contracts are kept intentionally consistent across all implementations to allow fair comparison between technologies.
