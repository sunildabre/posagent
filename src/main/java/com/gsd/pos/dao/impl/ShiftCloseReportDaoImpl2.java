package com.gsd.pos.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.gsd.pos.dao.ShiftCloseReportDao;
import com.gsd.pos.model.CarwashSales;
import com.gsd.pos.model.Discount;
import com.gsd.pos.model.FuelInventory;
import com.gsd.pos.model.FuelSales;
import com.gsd.pos.model.Payment;
import com.gsd.pos.model.ShiftReport;
import com.gsd.pos.model.Totals;
import com.gsd.pos.utils.Config;

public class ShiftCloseReportDaoImpl2 implements ShiftCloseReportDao {
	private static final Logger logger = Logger
			.getLogger(ShiftCloseReportDaoImpl2.class.getName());
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private Connection con;
	private Properties props; 
	
	public ShiftCloseReportDaoImpl2()  {
		init();
	}
	
	private  void init() {
		props = new Properties();
		try {
			File propsFile = new File("sqls.txt");
			if (propsFile.exists()) {
				logger.trace("loading file from current directory");
				props.load(new FileInputStream(propsFile));
			}
		} catch (IOException ex) {
			logger.warn(ex);
			logger.warn(ex.getMessage());
			ex.printStackTrace();
			throw new RuntimeException("COuld not find SQL file  sqls.txt in current directory " + System.getProperty("user.dir"));
		}
	}

	
	public ShiftReport getReport(Date dt) {
		ShiftReport report = new ShiftReport();
		try {
			con = DBHandler.getInstance().getConnection();
			String date = df.format(dt);
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
			setDiscounts(report, date, con);
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
			String sql=getSQL("shift_info");
			sql = sql.replace("#DATE#", date);
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
			String sql = getSQL("grand_total");
			sql = sql.replace("#DATE#", date);
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
			String sql = getSQL("fuel_totals");
			sql = sql.replace("#DATE#", date);
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
			String sql = getSQL("fuel_volumes");
			sql = sql.replace("#DATE#", date);
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
			String sql = getSQL("fuel_inventory");
			sql = sql.replace("#DATE#", date);
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
			String sql =getSQL("carwash_sales");
			sql = sql.replace("#DATE#", date);
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
			String sql = getSQL("payments");
			sql = sql.replace("#DATE#", date);
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
	
	private void setDiscounts(ShiftReport report, String date, Connection con)
			throws SQLException {
		PreparedStatement st = null;
		List<Discount> sales = new ArrayList<Discount>();
		report.setDiscounts(sales);
		try {
			String sql = getSQL("discounts");
			sql = sql.replace("#DATE#", date);
			logger.trace("Executing sql " + sql);
			st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Discount f = new Discount();
				f.setGrade(rs.getString(1));
				f.setCount(rs.getInt(2));
				f.setAmount(rs.getBigDecimal(3));
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
	
	private String getSQL(String sqlFor) {
		String sql  = props.getProperty(sqlFor);
		return sql;
		
	}
	
	
}
