package com.substring.chat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.corundumstudio.socketio.SocketIOServer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@SpringBootApplication(scanBasePackages = "com.substring.chat")
public class Application {

	@Autowired(required = false)
	private SocketIOServer socketIOServer;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@PostConstruct
	public void startSocketIoServer() {
		if (socketIOServer != null) {
			socketIOServer.start();
			System.out.println("Socket.IO server started on port " + socketIOServer.getConfiguration().getPort());
		}
	}
	
	@PreDestroy
	public void stopSocketIoServer() {
		if (socketIOServer != null) {
			socketIOServer.stop();
			System.out.println("Socket.IO server stopped");
		}
	}

}
