# Chat Application Backend

A production-ready real-time chat application built with Java, Spring Boot, MongoDB, WebSocket (STOMP + SockJS), JWT Authentication, and Spring Security.

## Features

- 🔐 **JWT Authentication & Authorization**
- 💬 **Real-time Messaging** with WebSocket
- 🗂️ **Private Chat Rooms**
- 📱 **RESTful APIs** for user and chat management
- 🔒 **Spring Security** with BCrypt password encryption
- 🛢️ **MongoDB** for data persistence
- 🔄 **Message Status Tracking** (SENT, DELIVERED, READ)

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Security**
- **Spring Data MongoDB**
- **Spring WebSocket + STOMP**
- **JWT (JSON Web Tokens)**
- **BCrypt** for password hashing
- **Lombok** for boilerplate reduction

## Project Structure

```
src/main/java/com/substring/chat/
├── config/                 # Configuration classes
│   ├── SecurityConfig.java
│   └── WebSocketConfig.java
├── controller/             # REST controllers
│   ├── AuthController.java
│   ├── ChatController.java
│   ├── MessageController.java
│   └── UserController.java
├── dto/                    # Data Transfer Objects
│   ├── AuthResponse.java
│   ├── ChatResponse.java
│   ├── MessageRequest.java
│   ├── MessageResponse.java
│   ├── UserLoginRequest.java
│   └── UserSignupRequest.java
├── exception/              # Exception handling
│   └── GlobalExceptionHandler.java
├── model/                  # Entity models
│   ├── Chat.java
│   ├── Message.java
│   └── User.java
├── repository/             # MongoDB repositories
│   ├── ChatRepository.java
│   ├── MessageRepository.java
│   └── UserRepository.java
├── security/               # Security components
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtUtil.java
├── service/                # Business logic services
│   ├── ChatService.java
│   ├── MessageService.java
│   └── UserService.java
├── websocket/              # WebSocket controllers
│   └── ChatWebSocketController.java
└── Application.java        # Main application class
```

## Prerequisites

- Java 21
- MongoDB (running on localhost:27017)
- Maven

## Setup Instructions

1. **Clone the repository**
2. **Start MongoDB** on default port (27017)
3. **Configure application.properties** (already configured for localhost)
4. **Build and run the application**:

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Signup
**POST** `/api/auth/signup`

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "mobile": "1234567890",
  "password": "password123",
  "profilePicture": "https://example.com/profile.jpg"
}
```

#### Login
**POST** `/api/auth/login`

```json
{
  "emailOrMobile": "john@example.com",
  "password": "password123"
}
```

### Users

#### Get all users (excluding logged-in user)
**GET** `/api/users`

*Requires Authentication Header: `Authorization: Bearer {token}`*

#### Get user profile
**GET** `/api/users/profile`

*Requires Authentication Header: `Authorization: Bearer {token}`*

#### Get user by ID
**GET** `/api/users/{userId}`

*Requires Authentication Header: `Authorization: Bearer {token}`*

### Chats

#### Get user's chats
**GET** `/api/chats`

*Requires Authentication Header: `Authorization: Bearer {token}`*

#### Get chat by ID
**GET** `/api/chats/{chatId}`

*Requires Authentication Header: `Authorization: Bearer {token}`*

### Messages

#### Send message
**POST** `/api/messages/send`

```json
{
  "receiverId": "receiver-user-id",
  "content": "Hello, how are you?"
}
```
*Requires Authentication Header: `Authorization: Bearer {token}`*

#### Get chat history (paginated)
**GET** `/api/messages/chat/{chatId}?page=0&size=50`

*Requires Authentication Header: `Authorization: Bearer {token}`*

#### Get all messages in chat
**GET** `/api/messages/chat/{chatId}/all`

*Requires Authentication Header: `Authorization: Bearer {token}`*

#### Update message status
**PUT** `/api/messages/{messageId}/status?status=READ`

*Requires Authentication Header: `Authorization: Bearer {token}`*

## WebSocket Endpoints

### Connect to WebSocket
**Endpoint:** `ws://localhost:8080/ws`

Use SockJS client to connect.

### Send Message via WebSocket
**Destination:** `/app/sendMessage`

```json
{
  "receiverId": "receiver-user-id",
  "content": "Hello via WebSocket!"
}
```

### Subscribe to Chat Messages
**Topic:** `/topic/chat/{chatId}`

Subscribe to receive real-time messages for a specific chat.

## Database Collections

- **users** - User profiles and authentication data
- **chats** - Chat room information
- **messages** - Message history

## Indexes

The following indexes are automatically created:
- `users.email` (unique)
- `users.mobile` (unique)
- `messages.chatId` (indexed for query performance)
- `messages.senderId` (indexed for query performance)
- `messages.timestamp` (indexed for sorting)

## Security

- All APIs except authentication endpoints require JWT token
- Passwords are hashed using BCrypt
- WebSocket connections are secured through authentication
- CORS is configured for frontend integration

## Postman Collection Examples

### 1. User Signup
```
POST http://localhost:8080/api/auth/signup
Content-Type: application/json

{
  "name": "Alice Smith",
  "email": "alice@example.com",
  "mobile": "9876543210",
  "password": "securePassword123",
  "profilePicture": "https://example.com/alice.jpg"
}
```

### 2. User Login
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "emailOrMobile": "alice@example.com",
  "password": "securePassword123"
}
```

### 3. Get All Users
```
GET http://localhost:8080/api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 4. Send Message (REST)
```
POST http://localhost:8080/api/messages/send
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "receiverId": "user-id-here",
  "content": "Hello! This is a test message."
}
```

### 5. Get Chat History
```
GET http://localhost:8080/api/messages/chat/chat-id-here?page=0&size=20
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## WebSocket Integration Example (JavaScript)

```javascript
// Connect to WebSocket
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to chat messages
    stompClient.subscribe('/topic/chat/chat-id-here', function(message) {
        const msg = JSON.parse(message.body);
        console.log('Received message:', msg);
    });
});

// Send message
function sendMessage(receiverId, content) {
    stompClient.send("/app/sendMessage", {}, JSON.stringify({
        'receiverId': receiverId,
        'content': content
    }));
}
```

## Environment Variables

Configure in `application.properties`:

```properties
# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=chat_app

# JWT
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Server
server.port=8080
```

## Testing

Run tests with:
```bash
mvn test
```

## Deployment

For production deployment:
1. Update JWT secret in `application.properties`
2. Configure production MongoDB connection
3. Set appropriate server port
4. Build jar: `mvn clean package`
5. Run: `java -jar target/chat.app.backend-0.0.1-SNAPSHOT.jar`

## Railway Deployment

This application is configured for deployment to Railway. Follow these steps to deploy:

1. **Prerequisites**:
   - Railway account
   - MongoDB Atlas or compatible database service
   - GitHub repository with your code

2. **Setup Environment Variables**:
   - `SPRING_MONGODB_URI`: Your MongoDB connection string
   - `JWT_SECRET`: Strong secret key for JWT tokens
   - `PORT`: Will be automatically set by Railway

3. **Deploy**:
   - Create new project on Railway
   - Connect to your GitHub repository
   - Add the environment variables
   - Deploy!

See the `RAILWAY_DEPLOYMENT_GUIDE.md` for detailed instructions.

## License

This project is for educational purposes.