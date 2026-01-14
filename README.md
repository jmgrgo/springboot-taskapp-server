# Spring Boot TaskApp Server

This repository contains the **Spring Boot** implementation of **TaskApp** (server layer).

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
5. User views tasks and tags by filters
   
## Domain Model (Subject to Change)

### User

- Email
- Password (stored as a hash)

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

This repository represents the **server** layer of TaskApp.

- Client repositories focus on UI, state management, and user interaction
- Server repositories focus on authentication, business logic, and APIs

All implementations follow the same domain rules to allow fair comparison.

## Technology Stack

- Java
- Spring Boot
- Spring Data
- Spring Security

## Related Repositories

Other implementations of TaskApp using different technologies:
- Frontend: Pending.
- Backend: Pending.

## Notes

The API contracts and application behavior are kept intentionally consistent across all implementations.
