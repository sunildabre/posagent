package com.gsd.pos.model;

import java.math.BigDecimal;

public class FuelSales {
	private Long shiftId;
	private Long fuelSalesId;
	private String grade;
	private String gradeName;
	private BigDecimal volume;
	private BigDecimal sales;
	private BigDecimal percentOfTotalSales;
	private BigDecimal totalFuelSales;
	private BigDecimal totalTax;
	private BigDecimal fuelDiscounts;
	private BigDecimal otherDiscounts;
	
	public Long getShiftId() {
		return shiftId;
	}
	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}
	public Long getFuelSalesId() {
		return fuelSalesId;
	}
	public void setFuelSalesId(Long fuelSalesId) {
		this.fuelSalesId = fuelSalesId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getSales() {
		return sales;
	}
	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}
	public BigDecimal getPercentOfTotalSales() {
		return percentOfTotalSales;
	}
	public void setPercentOfTotalSales(BigDecimal percentOfTotalSales) {
		this.percentOfTotalSales = percentOfTotalSales;
	}
	public BigDecimal getTotalFuelSales() {
		return totalFuelSales;
	}
	public void setTotalFuelSales(BigDecimal totalFuelSales) {
		this.totalFuelSales = totalFuelSales;
	}
	public BigDecimal getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}
	public BigDecimal getFuelDiscounts() {
		return fuelDiscounts;
	}
	public void setFuelDiscounts(BigDecimal fuelDiscounts) {
		this.fuelDiscounts = fuelDiscounts;
	}
	public BigDecimal getOtherDiscounts() {
		return otherDiscounts;
	}
	public void setOtherDiscounts(BigDecimal otherDiscounts) {
		this.otherDiscounts = otherDiscounts;
	}
	
	
	
}
