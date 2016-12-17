package com.gsd.pos.agent.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.gsd.pos.dao.ShiftCloseReportDao;
import com.gsd.pos.dao.impl.ShiftCloseReportDaoImpl;
import com.gsd.pos.model.ShiftReport;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VersionHandler implements HttpHandler{
	
	private static final Logger logger = Logger.getLogger(VersionHandler.class
			.getName());
	
	public void handle(HttpExchange exchange) throws IOException {
		URI uri = exchange.getRequestURI();
		logger.debug(uri.getPath());
		logger.debug(uri.getQuery());
		logger.debug(uri.getRawQuery());
		// Content
		@SuppressWarnings("unchecked")
		Headers resHeaders = exchange.getResponseHeaders();
		String content = "";
		try {
			content = "version 1.0.1";
			byte[] bytes = content.getBytes();
			// Set response headers explicitly
			resHeaders.add("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, bytes.length);
			OutputStream os = exchange.getResponseBody();
			os.write(bytes);
			os.close();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			content = "Unsupported Request [" + uri.getPath() + "/" + uri.getQuery() + "]";
			logger.warn("Recieved unsupported request [" + content + "] from  [" + exchange.getRemoteAddress() + "]" ); 
			byte[] bytes = content.getBytes();
			exchange.sendResponseHeaders(404, bytes.length);
			OutputStream os = exchange.getResponseBody();
			os.write(bytes);
			os.close();
		}
	}

	

}
