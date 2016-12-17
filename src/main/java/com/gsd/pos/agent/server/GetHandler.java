package com.gsd.pos.agent.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetHandler implements HttpHandler {
	public void handle(HttpExchange t) throws IOException {

		// add the required response header for a PDF file
		Headers h = t.getResponseHeaders();
		h.add("Content-Type", "application/pdf");

		// a PDF (you provide your own!)
		File file = new File("c:/temp/doc.pdf");
		byte[] bytearray = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(bytearray, 0, bytearray.length);

		// ok, we are ready to send the response.
		t.sendResponseHeaders(200, file.length());
		OutputStream os = t.getResponseBody();
		os.write(bytearray, 0, bytearray.length);
		os.close();
	}
}