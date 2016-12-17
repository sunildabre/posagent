package com.gsd.pos.model;

import java.math.BigDecimal;

public class Totals {
	private Long shiftId;
	private BigDecimal volume;
	private BigDecimal totalFuelSales;
	private BigDecimal totalDeptSales;
	private BigDecimal totalTax;
	private BigDecimal fuelDiscounts;
	private BigDecimal otherDiscounts;
	
	public Long getShiftId() {
		return shiftId;
	}
	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
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
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getTotalDeptSales() {
		return totalDeptSales;
	}
	public void setTotalDeptSales(BigDecimal totalDeptSales) {
		this.totalDeptSales = totalDeptSales;
	}
	
	
	
}
