package com.gsd.pos.dao;

import java.util.Date;
import com.gsd.pos.model.ShiftReport;

public interface ShiftCloseReportDao {
	
	public ShiftReport getReport(Date date) ;

}
