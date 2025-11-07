# Energy Management System – Distributed Systems Assignment 1

## Overview

This project implements an **Energy Management System** built using a **microservices architecture** that follows the **Request–Reply Communication Paradigm**.  
It allows authenticated users to **monitor and manage smart energy metering devices** via a **web-based frontend**, with services communicating through **REST APIs** and deployed using **Docker containers**.

The system ensures **secure access**, **role-based authorization and authentication**, and **loose coupling** between services through an **API Gateway** built as a **Reverse Proxy**.

## System Components

### 1. Frontend (ReactJS)

- Built using **React**, **JavaScript**, and **CSS**.
- Features a **bright pink and pastel theme**.
- Provides role-based pages:
  - **Admin Dashboard:** manage users, devices, and assign devices to users.
  - **User (Client) Dashboard:** view devices assigned to their account.

#### Start the Frontend

```bash
cd frontend
npm install
npm start
```
The frontend will be available at: http://localhost:3000

### 2. Microservices Layer

#### User Management Microservice

- Provides **CRUD operations** for managing users.  
- Stores user information in a **User Database**.  
- Verifies permissions and roles for protected operations.  
- Is available internally at: http://localhost:8081/user.

#### Device Management Microservice

- Provides **CRUD operations** for managing energy devices.  
- Inside **Device Database**, it stores information such as:  
  - `id`  
  - `name`  
  - `maximumConsumption`  
  - `yearOfManufacture`
- Manages **device–user associations**.  
- Communicates with the **User Management Microservice** for mapping relationships.  
- Is available internally at: http://localhost:8082/device.

#### Authentication Microservice

- Handles **user registration** and **login**.  
- Stores and verifies credentials in a **Credential Database**.  
- Generates **JWT tokens** for secure communication.  
- Prevents unauthorized access to restricted endpoints.  
- Acts as a **Middleware**: all requests to user or device microservices are redirected first to authentication microservice which validates the credentials and accepts or denies the communication.
- Is available internally at: http://localhost:8083/authentication/*.

Each microservice is implemented using **Spring Boot (Java)** and exposes **REST APIs**.

### 3. Reverse Proxy

- Acts as the **entry point** for all client requests.  
- Is available at: http://localhost:80.
- Routes each request to the correct **microservice**.  
- Validates **authentication tokens** and applies **authorization**.  
- Is implemented using **Traefik**.  
- Ensures that only **authenticated** and **authorized** users can access internal endpoints.
- It is also used as a **Load Balancer**.

## Docker Deployment

The system is fully containerized using **Docker** for modularity, scalability, and portability.

### Build Docker Images

Run the following commands inside each microservice folder:

```bash
docker build -t user-management-microservice .
docker build -t device-management-microservice .
docker build -t authentication-microservice .
```

For the React frontend:

```bash
docker build -t frontend .
```

### Run All Containers

Use **Docker Compose** to run all services together:

```bash
docker-compose up --build
```

This will:
- Build all microservices.
- Launch all microservices and PostgreSQL databases.
- Connect all containers in a shared **Docker network**.
- Expose the microservices on the ports defined in **docker-compose.yml** file.

To stop the containers:

```bash
docker-compose down
```

## Application Flow

1. The user registers or logs in through the **frontend**.
2. The **Reverse Proxy** sends the credentials to the **Authentication Microservice** for validation.
2. The **Authentication Microservice** verifies credentials and issues a **JWT token**.
4. If the validation succeedes, **Reverse Proxy** forwards the request.
5. Requests are routed to the appropriate microservice:
    - **Admin**: performs CRUD operations on users and devices; assigns devices to users.
    - **User** (**Client**): views assigned devices.