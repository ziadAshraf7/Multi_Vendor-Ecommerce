# Multi-Vendor eCommerce Platform

This project is a **multi-vendor eCommerce platform** built using **Spring**, **Spring Boot**, **Spring Security**, **Hibernate**, and **Redis**. The project leverages a **MySQL** database with **Flyway** for version control and smooth database migrations. The application supports multiple vendors, product management, and secure user authentication.

## Features Implemented

### 1. Database Schema and API Design
I have designed and built a robust database schema that includes the following tables:
- **Product**
- **User**
- **Brand**
- **Category**
- **Vendor_Product**
- **Vendor_Product_Image**
- **Cart**
- **CartItem**
- **ProductReview**
- **Sub_Category_Attribute**
- **Product_Attribute_Value**

Each table has been fully integrated with corresponding services and REST APIs, ensuring complete functionality for product and vendor management.

### 2. Authentication and Security
- Implemented user authentication using **JWT (JSON Web Tokens)**.
- Managed anonymous user cart sessions using **Redis**, enabling seamless shopping experiences for non-logged-in users.
- Implemented functionality to link an anonymous user's cart with their user account upon login.
- Secured the application by protecting sensitive endpoints, ensuring only authenticated users and vendors have access where appropriate.

## Upcoming Features

### 1. Performance Optimization
- **Enhancing Hibernate queries** to optimize database performance and improve response times.

### 2. Coupons and Orders
- Adding support for **coupons**, allowing vendors to offer discounts on products.
- Designing and integrating an **Order** table, with plans to integrate **PayPal** as a payment gateway for secure transactions.

### 3. Vendor-Admin Notification System
- Implementing a **notification system** to handle product approval workflows, allowing vendors to submit products for admin review and approval.

## Technologies Used

- **Spring Boot**: Core framework for building the application.
- **Spring Security**: Handles authentication and security across the platform.
- **Hibernate**: ORM used for database interactions.
- **Redis**: Used for session management, particularly for anonymous users.
- **MySQL**: Relational database used to store all eCommerce data.
- **Flyway**: Manages database versioning and migrations.
- **JWT**: Used for secure authentication.
- **PayPal**: Planned integration for payment processing.


