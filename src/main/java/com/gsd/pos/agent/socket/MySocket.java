package com.gsd.pos.agent.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class MySocket {
	public static void main(String[] args) throws IOException {
		System.setProperty("javax.net.debug", "ssl");
		System.setProperty("javax.net.ssl.keyStore", "posagent.ks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
		System.setProperty("javax.net.ssl.trustStore", "posagent.ks");
		int port = 9090;
		ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
		ServerSocket ssocket = ssocketFactory.createServerSocket(port);
		// Listen for connections
		while (true) {
			Socket socket = ssocket.accept();
			new Thread(new ProcessThread(socket)).start();
		}
	}
	
}
