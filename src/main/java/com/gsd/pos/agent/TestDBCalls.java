package com.gsd.pos.agent;

import java.util.Date;

import com.google.gson.Gson;
import com.gsd.pos.dao.ShiftCloseReportDao;
import com.gsd.pos.dao.impl.ShiftCloseReportDaoImpl;
import com.gsd.pos.model.ShiftReport;

public class TestDBCalls {

	public static void main(String[] args) {

		ShiftCloseReportDao dao = new ShiftCloseReportDaoImpl();
		ShiftReport report = dao.getReport(new Date());
		Gson gson = new Gson();
		System.out.println(gson.toJson(report));

	}
}
