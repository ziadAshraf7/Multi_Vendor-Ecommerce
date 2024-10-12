# Multi-Vendor eCommerce Platform

## Overview

This project is a multi-vendor eCommerce platform built using modern **Java** technologies, including **Spring**, **Spring Boot**, **Spring Security**, **Hibernate**, **Redis** , **WebSocket** and **Apache Kafka** . The platform leverages a **MySQL** database with **Flyway** integration for seamless migrations and version control of the schema.

## Current Features

### Database Schema & API
The project includes a robust and scalable database schema that supports key eCommerce functionalities. The following tables have been implemented, with their respective services and REST APIs:
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

### Authentication & Security
- **JWT Authentication**: Implemented secure authentication with JSON Web Tokens (JWT) for authorized users.
- **Anonymous Cart Management**: Handled session management for anonymous users using Redis, enabling seamless cart operations before login.
- **Cart Linking**: Upon user login, the system automatically links the anonymous user's cart to the logged-in user's cart.
- **Endpoint Security**: Ensured the application’s security by protecting sensitive API endpoints.

## Additional Features

### WebSocket Integration
- **Real-time Communication**: Implemented WebSocket connections for real-time communication between vendors and the admin. This allows instant notifications and updates on product creation requests.

### Notification System with Apache Kafka
- **Message Broker**: Integrated Apache Kafka as a message broker to manage the notification system efficiently. 
- **Vendor-Admin Notifications**: Notifications are sent between vendors and the admin regarding product creation requests, including approval or rejection responses.
- **Manual Acknowledgment**: Configured manual acknowledgment in the Kafka consumer settings to ensure that notification messages can be sent to users who are offline. 
- **Reconnection Handling**: Offline users receive their pending notifications when they reconnect through the WebSocket.

## Upcoming Features

- **Coupons**: Introduce a coupon system to allow discounts on products.
- **Order Management**: Add an `Order` table to manage customer purchases and transactions.
- **Payment Gateway Integration**: Integrate PayPal for secure and reliable payment processing.
- **Testing & QA**: Implement comprehensive unit and integration tests to ensure functionality.
- **CI/CD Pipelines**: Set up continuous integration and delivery pipelines for streamlined deployment.
- **Database Replication**: Implement database replication for improved scalability and fault tolerance.

## Technology Stack

- **Backend**: Spring, Spring Boot, Spring Security
- **Persistence**: Hibernate, MySQL
- **Session Management**: Redis
- **Migration**: Flyway
- **Authentication**: JWT (JSON Web Tokens)
- **Message Broker**: Apache Kafka
- **WebSocket**: Real-time communication
- **Payment Gateway**: PayPal (upcoming)
- **Build Tools**: Maven

## Future Enhancements

- **Performance Tuning**: Continued focus on improving the system's overall performance, including query optimizations and load handling.
- **Additional Features**: Expansion of the platform's functionality with features such as advanced search, recommendation systems, and more.




