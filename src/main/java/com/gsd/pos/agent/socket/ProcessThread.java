package com.gsd.pos.agent.socket;

import static com.gsd.pos.utils.Utils.make4CharsLong;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import com.google.gson.Gson;
import com.gsd.pos.Message;

public class ProcessThread implements Runnable {
	private Socket socket = null;

	public ProcessThread(Socket socket) {
		// super("ProcessThread");
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream in = socket.getInputStream();
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			char[] lenArray = new char[4];
			r.read(lenArray, 0, 4);
			int len = Integer.valueOf(new String(lenArray));
			System.out.println("Got length [" + len + "]");
			char[] message = new char[len];
			String response = null;
			r.read(message, 0, len);
			response = process(new String(message));
			String fourCharsLength = make4CharsLong(response.toCharArray().length);
			w.write(fourCharsLength, 0, fourCharsLength.length());
			w.write(response, 0, response.toCharArray().length);
			w.newLine();
			w.flush();
			w.write(response, 0, response.length());
			w.newLine();
			w.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}

	private String process(String message) {
		System.out.println(message);
		Gson gson = new Gson();
		Message m = gson.fromJson(message, Message.class);
		System.out.println(m.getName());
		return message;
	}
}