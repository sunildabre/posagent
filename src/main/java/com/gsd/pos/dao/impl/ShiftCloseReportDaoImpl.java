package com.gsd.pos.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.gsd.pos.dao.ShiftCloseReportDao;
import com.gsd.pos.model.CarwashSales;
import com.gsd.pos.model.FuelInventory;
import com.gsd.pos.model.FuelSales;
import com.gsd.pos.model.Payment;
import com.gsd.pos.model.ShiftReport;
import com.gsd.pos.model.Totals;

public class ShiftCloseReportDaoImpl implements ShiftCloseReportDao {
	private static final Logger logger = Logger
			.getLogger(ShiftCloseReportDaoImpl.class.getName());
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public ShiftReport getReport(Date dt) {
		Connection con = null;
		ShiftReport report = new ShiftReport();
		try {
			String date = df.format(dt);
			con = DBHandler.getInstance().getConnection();
			setShiftInfo(report, date, con);
			setFuelVolumes(report, date, con);
			setFuelTotals(report, date, con);
			// setFuelSales(report, date, con);
			setFuelInventory(report, date, con);
			setGradeNames(report, con);
			setPayments(report, date, con);
			setStoreInfo(report, con);
			setGrandTotal(report, date, con);
			setCarwashSales(report, date, con);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
			}
		}
		return report;
	}

	private void setShiftInfo(ShiftReport report, String date, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		try {
			String sql = "SELECT strt_dt, end_dt"
					+ " FROM globalstore.dbo.prd_aggr" + " WHERE '" + date
					+ " 12:00:00' BETWEEN strt_dt" + " AND end_dt"
					+ " AND tbl_cd = 97 ";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				report.setStartTime(rs.getTimestamp(1));
				report.setEndTime(rs.getTimestamp(2));
			}
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}

	}

	private void setGrandTotal(ShiftReport report, String date, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		try {
			String sql = "SELECT fuel_total_amount+disc_amount+dept_sls_amount+sls_tax_amount"
					+ " FROM globalstore.dbo.CS_NON_RESET_TOTALS(NOLOCK)"
					+ " WHERE CS_NON_RESET_TOTALS.prd_aggr_id = ("
					+ "		SELECT prd_aggr.prd_aggr_id"
					+ "		FROM globalstore.dbo.prd_aggr"
					+ "		WHERE '"
					+ date
					+ " 12:00:00' BETWEEN strt_dt"
					+ "				AND end_dt"
					+ "			AND tbl_cd = 7" + "		)" + "and subprd_nbr = 1";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				report.setGrandTotal(rs.getBigDecimal(1));
			}
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}

	private void setFuelTotals(ShiftReport report, String date, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		try {
			String sql = "SELECT sum(fuel_total_vol)"
					+ "	,sum(fuel_total_amount)"
					+ "	,sum(fuel_disc_amount)"
					+ "	,sum(dept_sls_amount)"
					+ "	,sum(disc_amount-fuel_disc_amount) as \"Other Discounts\""
					+ "	,sum(sls_tax_amount)"
					+ " FROM globalstore.dbo.CS_NON_RESET_TOTALS(NOLOCK)"
					+ " WHERE CS_NON_RESET_TOTALS.prd_aggr_id = ("
					+ "		SELECT prd_aggr.prd_aggr_id"
					+ "		FROM globalstore.dbo.prd_aggr" + "		WHERE '" + date
					+ " 12:00:00' BETWEEN strt_dt" + "				AND end_dt"
					+ "			AND tbl_cd = 7" + "		)" + "	AND subprd_nbr != 0";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Totals t = new Totals();
				t.setVolume(rs.getBigDecimal(1));
				t.setTotalFuelSales(rs.getBigDecimal(2));
				t.setFuelDiscounts(rs.getBigDecimal(3));
				t.setTotalDeptSales(rs.getBigDecimal(4));
				t.setOtherDiscounts(rs.getBigDecimal(5));
				t.setTotalTax(rs.getBigDecimal(6));
				report.setTotals(t);
			}
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}

	private void setFuelVolumes(ShiftReport report, String date, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		List<FuelSales> sales = new ArrayList<FuelSales>();
		report.setFuelSales(sales);
		try {
			String sql = "SELECT itm_id, sum(fuel_total_vol),"
					+ " sum(fuel_total_amount)"
					+ " FROM globalstore.dbo.CS_NON_RESET_FUEL_TOTALS(NOLOCK)"
					+ " WHERE CS_NON_RESET_FUEL_TOTALS.prd_aggr_id = ("
					+ "		SELECT prd_aggr.prd_aggr_id"
					+ "		FROM globalstore.dbo.prd_aggr" + "		WHERE '" + date
					+ " 12:00:00' BETWEEN strt_dt" + "				AND end_dt"
					+ "			AND tbl_cd = 7" + "		)" + "	AND subprd_nbr != 0"
					+ " group by itm_id";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				FuelSales f = new FuelSales();
				f.setGrade(rs.getString(1));
				f.setVolume(rs.getBigDecimal(2));
				f.setSales(rs.getBigDecimal(3));
				sales.add(f);
			}
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}

	private void setFuelInventory(ShiftReport report, String date,
			Connection con) throws SQLException {
		PreparedStatement st = null;
		List<FuelInventory> inventory = new ArrayList<FuelInventory>();
		report.setFuelInventory(inventory);
		try {
/*			String sql = "SELECT  datetime, TankID, ProductName, Volume FROM    globalstore.dbo.G_Fuel_TankMonitorReading GFT "
					+ "INNER JOIN "
					+ "(   SELECT  periodid, MAX(datetime) AS last_shift FROM  globalstore.dbo.G_Fuel_TankMonitorReading  GROUP BY periodid  ) MaxPeriod"
					+ " ON GFT.periodid = MaxPeriod.periodid  AND GFT.datetime = MaxPeriod.last_shift"
					+ " where GFT.periodid = ( SELECT prd_aggr.prd_aggr_id FROM globalstore.dbo.prd_aggr"
					+ " WHERE '"
					+ date
					+ "  12:00:00' BETWEEN strt_dt    AND end_dt AND tbl_cd = 7  )"
					+ " and subperiodid = 0";
*/			//Changed 12/14/2016 
			String sql = "SELECT datetime ,  TankID, ProductName, Volume FROM globalstore.dbo.G_Fuel_TankMonitorReading  "
					+ " WHERE G_Fuel_TankMonitorReading.periodid = ("
					+ " SELECT prd_aggr.prd_aggr_id FROM globalstore.dbo.prd_aggr  WHERE '" + date + " 12:00:00' BETWEEN strt_dt  AND end_dt  AND tbl_cd = 97 )"
					+ " and subperiodid = 0" ;
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				FuelInventory f = new FuelInventory();
				f.setDatetime(rs.getTimestamp(1));
				f.setTankId(rs.getLong(2));
				f.setGradeName(rs.getString(3));
				f.setVolume(rs.getBigDecimal(4));
				inventory.add(f);
			}
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}

	private void setCarwashSales(ShiftReport report, String date, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		List<CarwashSales> sales = new ArrayList<CarwashSales>();
		report.setCarwashSales(sales);
		try {
			String sql = "Select SUM(sls_amount) as salesamount, SUM(sls_cnt) as salescount, "
					+ "   SUM(rtn_amount) as refundamount, SUM(rtn_cnt) as refundcount,  "
					+ "   SUM(disc_amount) as discamount, SUM(disc_cnt) as disccount, "
					+ " SUM(sls_amount)+   SUM(rtn_amount)+   SUM(disc_amount) as netsales "
					+ "   From globalstore.dbo.CS_CARWASH_SLS_CUR  "
					+ "   WHERE CS_CARWASH_SLS_CUR.PRD_AGGR_ID    = ( "
					+ "        SELECT prd_aggr.prd_aggr_id "
					+ "        FROM globalstore.dbo.prd_aggr "
					+ "        WHERE '"
					+ date
					+ " 12:00:00' BETWEEN strt_dt "
					+ "                AND end_dt "
					+ "            AND tbl_cd = 7 "
					+ "        ) AND  "
					+ "   CS_CARWASH_SLS_CUR.SUBPRD_NBR = 0 AND "
					+ "   CS_CARWASH_SLS_CUR.package_id IN (SELECT PKG_ID FROM globalstore.dbo.CS_WASH_PKG) AND "
					+ "   CS_CARWASH_SLS_CUR.PLU in (SELECT OPTIONS_STR.OPT_VALUE FROM globalstore.dbo.OPTIONS_STR WHERE OPTIONS_STR.OPT_ID in (13009,13010))";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				CarwashSales f = new CarwashSales();
				f.setGrossSales(rs.getBigDecimal("salesamount"));
				f.setItemCount(rs.getLong("salescount"));
				f.setNetSales(rs.getBigDecimal("netsales"));
				// f.setNetCount(rs.getLong("salescount"));
				f.setDiscount(rs.getBigDecimal("discamount"));
				f.setDiscountCount(rs.getLong("disccount"));
				f.setRefund(rs.getBigDecimal("refundamount"));
				f.setRefundCount(rs.getLong("refundcount"));

				if (f.getDiscountCount() != null) {
					if (f.getItemCount() != null) {
						f.setNetCount(f.getItemCount() - f.getDiscountCount());
						logger.debug(String.format(
								"Counts Item %s , Refund %s , Net %s",
								f.getItemCount() + "", f.getRefundCount() + "",
								f.getNetCount() + ""));
					}
				}
				sales.add(f);
			}
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}

	private void setGradeNames(ShiftReport report, Connection con) {
		PreparedStatement st = null;
		try {
			String sql = "SELECT  plu_id, dspl_descr "
					+ " FROM  globalstore.dbo.plu (NoLock) "
					+ " WHERE  plu_id like 'Grade 0%' ";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {

				String grade = rs.getString(1);
				FuelSales f = getFuelSales(report.getFuelSales(), grade);
				if (f != null) {
					f.setGradeName(rs.getString(2));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}

	private FuelSales getFuelSales(List<FuelSales> fuelSales, String grade) {
		for (FuelSales s : fuelSales) {
			if (grade.equalsIgnoreCase(s.getGrade())) {
				return s;
			}
		}
		return null;
	}

	private void setStoreInfo(ShiftReport report, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		try {
			String sql = "SELECT ln_1 	,cty	,st	,pst_cd FROM globalstore.dbo.address WHERE addr_id = 1";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				report.setStreet(rs.getString(1));
				report.setCity(rs.getString(2));
				report.setState(rs.getString(3));
				report.setZip(rs.getString(4));

			}

			sql = "SELECT str_nm	,str_cd FROM globalstore.dbo.store"
					+ " WHERE str_id = 1 ";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				report.setStoreName(rs.getString(1));
				report.setStoreNumber(rs.getString(2));
			}

		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}

	private void setPayments(ShiftReport report, String date, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		List<Payment> sales = new ArrayList<Payment>();
		report.setPayments(sales);
		try {
			String sql = "SELECT TENDER.TND_DESCR AS TndDesc"
					+ "	,sum(CS_ITEMTYPE_SLS_BY_TND.INSIDE_SLS_AMT) + sum(CS_ITEMTYPE_SLS_BY_TND.OUTSIDE_SLS_AMT) AS Amount"
					+ " FROM globalstore.dbo.CS_ITEMTYPE_SLS_BY_TND(NOLOCK)"
					+ " INNER JOIN globalstore.dbo.TENDER(NOLOCK)"
					+ "	ON CS_ITEMTYPE_SLS_BY_TND.TND_CD = TENDER.TND_CD"
					+ " WHERE CS_ITEMTYPE_SLS_BY_TND.PRD_AGGR_ID = ("
					+ "		SELECT prd_aggr.prd_aggr_id"
					+ "		FROM globalstore.dbo.prd_aggr" + "		WHERE '" + date
					+ " 12:00:00' BETWEEN strt_dt" + "				AND end_dt"
					+ "			AND tbl_cd = 7" + "		)"
					+ "	AND CS_ITEMTYPE_SLS_BY_TND.SUBPRD_NBR != 0"
					+ " GROUP BY TENDER.TND_DESCR" + "";
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Payment f = new Payment();
				f.setType(rs.getString(1));
				f.setAmount(rs.getBigDecimal(2));
				sales.add(f);
			}

		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException se) {
			}
		}
	}
}
