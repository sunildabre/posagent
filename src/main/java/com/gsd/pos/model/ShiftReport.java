package com.gsd.pos.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ShiftReport {
	private Long shiftId;
	private Long siteId;
	private Date startTime;
	private Date endTime;
	private String operatorName;
	private Long posShiftId;
	private Long operatorId;
	private List<FuelSales> fuelSales;
	private List<FuelInventory> fuelInventory;
	private List<CarwashSales> carwashSales;
	private List<Payment> payments;
	private Totals totals;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String storeNumber;
	private String storeName;
	private BigDecimal grandTotal;

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public Long getShiftId() {
		return shiftId;
	}
	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Long getPosShiftId() {
		return posShiftId;
	}
	public void setPosShiftId(Long posShiftId) {
		this.posShiftId = posShiftId;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public List<FuelSales> getFuelSales() {
		return fuelSales;
	}
	public void setFuelSales(List<FuelSales> fuelSales) {
		this.fuelSales = fuelSales;
	}
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	public Totals getTotals() {
		return totals;
	}
	public void setTotals(Totals totals) {
		this.totals = totals;
	}
	public String getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public List<CarwashSales> getCarwashSales() {
		return carwashSales;
	}
	public void setCarwashSales(List<CarwashSales> carwashSales) {
		this.carwashSales = carwashSales;
	}
	public List<FuelInventory> getFuelInventory() {
		return fuelInventory;
	}
	public void setFuelInventory(List<FuelInventory> fuelInventory) {
		this.fuelInventory = fuelInventory;
	}
}
