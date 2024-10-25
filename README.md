# Ride-Sharing Platform

**College ID:** IIT2021177

## Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Prerequisites](#prerequisites)
5. [Setup Instructions](#setup-instructions)
6. [Running the Project](#running-the-project)
7. [Deployment](#deployment)
8. [Assumptions and Limitations](#assumptions-and-limitations)
9. [Links](#links)

## Project Overview
This project is a Ride-Sharing Platform that enables users to share and track rides through distinct roles: Traveler, Traveler Companion, and Admin. The platform is designed to facilitate seamless ride-sharing experiences, including sharing ride details, tracking rides, and providing feedback. Built using ReactJS for the frontend, Spring Boot (Java) for the backend, and H2 Database, this application prioritizes user security and scalability.

![image](https://example.com/path/to/your/image.png)

## Features
- **Traveler Functionalities:**
  - Share ride details (Trip ID, Driver Name, Driver Phone Number, Cab Number) via WhatsApp or SMS.
  - Audit trail to review rides shared with companions.
  
- **Traveler Companion Functionalities:**
  - Real-time tracking of the traveler's ride.
  - Notifications when the ride is complete and when the cab is near the drop-off location.
  - Ability to provide feedback on the ride experience to the Admin.

- **Admin Functionalities:**
  - View all rides shared by users.
  - Access overall feedback from traveler companions regarding ride experiences.
  - Monitor user interactions and system health.

- **Security and Scalability:**
  - Robust authentication measures to ensure account security.
  - Scalable strategies to accommodate a growing user base.

## Tech Stack
**Frontend:**
- ReactJS (for building the user interface)

**Backend:**
- Spring Boot (Java) (for creating RESTful APIs)

**Database:**
- **H2 Database**: Act as a SQL database wrapper using Spring Boot, facilitating efficient storage.

### Cost Estimation

- **Space Complexity**: The GET operation involves reading from the database without additional space consumption. In contrast, POST is used to send data to a server to create/update a table.
- **Time Complexity**: The search operation for GET can take up to the number of existing versions, while the put operation, involving adding a column to the database, is executed in constant time.

### **Object-Oriented Programming Language (OOPS)**

- **Abstraction and Encapsulation**: Robust OOPS principles are followed, with extensive abstraction in the backend. Multiple models are created to abstract and encapsulate data storage, enhancing system organization.                                 
- **Polymorphism**: Implemented to ensure flexibility and adaptability within the codebase.
- **Modularization**: The code is highly modularized, enabling easy addition of new functionality. Each feature is organized into its own class, simplifying debugging and maintenance.

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Java JDK (version 11 or higher)
- Maven
- Spring Boot (for backend)
- Node.js (version 14.x or higher) and npm (for frontend)
- H2 database server running

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/shiva177/Ride-sharing-Backend.git]
   cd ride-sharing-platform
```

2. **Build and Run the Backend:**

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will run on **`http://localhost:8123`**.

