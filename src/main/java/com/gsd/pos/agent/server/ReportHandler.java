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

public class ReportHandler implements HttpHandler{
	
	private static final Logger logger = Logger.getLogger(ReportHandler.class
			.getName());
	private static DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
	
	public void handle(HttpExchange exchange) throws IOException {
		String method = exchange.getRequestMethod();
		URI uri = exchange.getRequestURI();
		logger.debug(uri.getPath());
		logger.debug(uri.getQuery());
		logger.debug(uri.getRawQuery());
		// Content
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>)exchange.getAttribute("parameters");
		Headers resHeaders = exchange.getResponseHeaders();
		String content = "";
		try {
			content = getReport(params);
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

	private String getReport(Map<String, Object> params) throws UnsupportedOperationException{
		Object value = params.get("name");
		if ((value != null) && ("shift_close".equalsIgnoreCase(value.toString()))) {
			String formattedDate = "";
			Date dt = null;
			value = params.get("date");
			if ((value != null)) {
				formattedDate = value.toString();
				try {
					dt = df.parse(formattedDate);
				} catch (ParseException e) {
					logger.warn(e);
					dt = new Date();
				}
			}  else {
				dt = new Date();
			}
			return getShiftCloseReport(dt);
		}
		throw new UnsupportedOperationException();
	}
	
	
	

	private String getShiftCloseReport(Date date) {
		ShiftCloseReportDao dao = new ShiftCloseReportDaoImpl();
		ShiftReport report =  dao.getReport(date);
		Gson gson = new Gson();
		return gson.toJson(report);
	}

}
