# Socket.IO Integration Guide for React Frontend

This guide explains how to connect your React frontend to the Socket.IO server running on the backend.

## Backend Configuration

The backend Socket.IO server runs on port `9092` and expects JWT tokens for authentication.

## React Frontend Implementation

Install the Socket.IO client library:
```bash
npm install socket.io-client
```

Create a Socket.IO service in your React app:

```javascript
import io from 'socket.io-client';

class SocketIoService {
  constructor() {
    this.socket = null;
  }

  connect(token) {
    this.socket = io('http://localhost:9092', {
      transports: ['websocket'],
      query: {
        token: `Bearer ${token}`  // Pass JWT token for authentication
      }
    });

    this.socket.on('connect', () => {
      console.log('Connected to Socket.IO server');
    });

    this.socket.on('disconnect', () => {
      console.log('Disconnected from Socket.IO server');
    });

    // Listen for incoming messages
    this.socket.on('receive_message', (message) => {
      console.log('Received message:', message);
      // Handle the received message in your UI
    });
  }

  disconnect() {
    if (this.socket) {
      this.socket.disconnect();
    }
  }

  // Join a chat room
  joinChat(chatId) {
    if (this.socket) {
      this.socket.emit('join_chat', chatId);
    }
  }

  // Leave a chat room
  leaveChat(chatId) {
    if (this.socket) {
      this.socket.emit('leave_chat', chatId);
    }
  }

  // Send a message
  sendMessage(receiverId, content) {
    if (this.socket) {
      const messageData = {
        receiverId: receiverId,
        content: content
      };
      this.socket.emit('send_message', messageData);
    }
  }
}

export default new SocketIoService();
```

## Usage in React Component

```javascript
import React, { useEffect, useState } from 'react';
import socketIoService from './services/SocketIoService';

const ChatComponent = ({ userToken }) => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    // Connect to Socket.IO server when component mounts
    socketIoService.connect(userToken);

    // Listen for incoming messages
    socketIoService.socket.on('receive_message', (message) => {
      setMessages(prev => [...prev, message]);
    });

    // Cleanup on component unmount
    return () => {
      socketIoService.disconnect();
    };
  }, [userToken]);

  const handleSendMessage = (receiverId, content) => {
    socketIoService.sendMessage(receiverId, content);
  };

  return (
    <div>
      {/* Your chat UI */}
    </div>
  );
};

export default ChatComponent;
```

## API Flow

1. User authenticates via REST API (`/api/auth/login`) to get JWT token
2. Connect to Socket.IO server with the JWT token
3. Send messages via Socket.IO events (`send_message`)
4. Receive real-time messages via Socket.IO events (`receive_message`)
5. For persistence, also use REST API (`/api/messages/send`) for guaranteed delivery

## Event Reference

- `send_message`: Send a message to another user
- `receive_message`: Receive a message from another user
- `join_chat`: Join a specific chat room
- `leave_chat`: Leave a specific chat room

The Socket.IO server is now integrated with your existing message persistence, so all messages are stored in MongoDB and broadcasted in real-time via Socket.IO.