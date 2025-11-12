# LelangkuSenang - Spring Boot Auction Web Application

LelangkuSenang (My Happy Auction) is a full-featured online auction web application built with Spring Boot and Thymeleaf. It provides a comprehensive platform for users to register, list items for auction, place bids, manage transactions, and communicate in real-time.

## Features

- **User Authentication:** Secure user registration and login system using Spring Security and session management.
- **Item Management (CRUD):** Users can add, edit, view, and delete their own items (goods) for auction.
- **Auction System:**
  - Sellers can start auctions for their "gudang" (warehouse) items.
  - Buyers can browse active auctions.
  - Buyers can place bids in predefined increments.
  - A "Buy Now" option is available to purchase an item immediately at a set price.
- **Transaction Management:**
  - Automatic transaction creation when an auction is won or "Buy Now" is used.
  - Separate views for "Seller Transactions" and "Buyer Transactions," filterable by status (Pending, Failed, Success).
  - Buyers can upload proof of payment (image upload).
  - Sellers can view transaction details, including buyer/seller info and payment status.
- **Real-time Chat:**
  - WebSocket-based real-time chat for user-to-user communication.
  - Users can search for other users to start a new chat.
  - View chat history and send messages in a dedicated chat detail view.
- **User Profiles:** Users can view and update their profile information, including name, email, address, and profile picture.
- **Scheduled Tasks:**
  - `LelangSchedulerService` automatically checks for auctions that have passed their deadline, marking them as `TERJUAL` (if there's a winner) or `KADALUARSA` (if not).
  - The scheduler also fails pending transactions that have passed their payment deadline.
- **Dynamic Address Forms:**
  - Integrates with the **GoAPI** external API to dynamically fetch Indonesian regional data (provinces, cities, districts, villages) for address forms during registration and profile updates.

## Tech Stack

- **Backend:**
  - Java 17+
  - Spring Boot (Spring Web, Spring Data JPA, Spring Security)
  - Spring WebSocket
- **Frontend:**
  - Thymeleaf (Server-Side Template Engine)
  - Tailwind CSS (Utility-First CSS Framework)
  - jQuery & JavaScript (DOM Manipulation, AJAX, WebSocket Client)
- **Database:**
  - MySQL (as configured in `application.yml`)
- **Build:**
  - Maven (implied, standard for Spring Boot)

## Setup and Installation

### 1. Prerequisites

- **Java JDK 17** or newer
- **Apache Maven**
- **MySQL Server**
- **GoAPI Key:** An API key from [https://goapi.io/](https://goapi.io/) for the regional address data.

### 2. Database Configuration

1.  Start your MySQL server.
2.  Create a new database. The project is configured to use `proyekuas`:
    ```sql
    CREATE DATABASE proyekuas;
    ```
3.  Open the `src/main/resources/application.yml` file.
4.  Update the `spring.datasource` properties to match your MySQL setup:
    ```yaml
    spring:
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        username: YOUR_MYSQL_USERNAME
        password: YOUR_MYSQL_PASSWORD
        url: jdbc:mysql://localhost:3306/proyekuas # Ensure the database name matches
    ```
5.  The application uses `spring.jpa.hibernate.ddl-auto: update`, so tables will be automatically created or updated on the first run.

### 3. API Key Configuration

1.  Sign up at [https://goapi.io/](https://goapi.io/) to get your free API key.
2.  Open `src/main/resources/application.yml`.
3.  Find the `api.goapi.key` property and paste your key:
    ```yaml
    api:
      goapi:
        key: "YOUR_GOAPI_KEY_HERE"
    ```

### 4. File Storage Configuration

This project saves uploaded files (profile pictures, item photos, payment proofs) to absolute file paths on the C: drive. You **must** update these paths in the following files to match your operating system and desired directory structure:

- `src/main/java/proyekuas/uas/service/impl/BarangServiceImpl.java`
  - Update `directoryPath` in `addBarang()` and `updateBarang()`.
- `src/main/java/proyekuas/uas/service/impl/TransaksiServiceImpl.java`
  - Update `directoryPath` in `konfirmasiPembayaran()`.
- `src/main/java/proyekuas/uas/service/impl/UserServiceImpl.java`
  - Update `directoryPath` in `updateUser()`.

**Recommendation:** A better practice would be to use a relative path within the project or configurable application properties for these directories.

### 5. Running the Application

1.  Open a terminal in the project's root directory.
2.  Run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```
3.  Once the application has started, access it in your browser at:
    `http://localhost:8080/`

## Project Structure (Overview)
```
src
├── main
│ ├── java
│ │ └── proyekuas
│ │ └── uas
│ │ ├── config # SecurityConfig, WebSocketConfig
│ │ ├── controller # Web Controllers (MVC)
│ │ ├── dto # Data Transfer Objects (e.g., RegisterForm)
│ │ ├── entity # JPA Entities (User, Barang, Lelang, etc.)
│ │ ├── repository # Spring Data JPA Repositories
│ │ ├── service # Business Logic (Interfaces and Impl)
│ │ ├── websocket # ChatEndpoint (Server-side WebSocket)
│ │ └── UasApplication.java # Main class
│ └── resources
│ ├── static
│ │ ├── css
│ │ ├── fotobarang
│ │ ├── fotoprofil
│ │ └── icon
│ ├── templates # Thymeleaf HTML files
│ └── application.yml # Application configuration
└── test
```
## License

This project is licensed under the MIT License.
