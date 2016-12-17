package com.gsd.pos.agent;

import com.gsd.pos.agent.server.MyServer;

public class Main {
	public static void main(String[] args) {
		try {
			new Main().start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start(String args[]) throws Exception {
		new MyServer().start();
	}
}
