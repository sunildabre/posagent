package com.gsd.pos.model;

import java.math.BigDecimal;

public class CarwashSales {
	private Long shiftId;
	private Long carwashSalesId;
	private BigDecimal netSales;
	private BigDecimal discount;
	private BigDecimal grossSales;
	private BigDecimal refund;
	private Long itemCount;
	private Long refundCount;
	private Long netCount;
	private Long discountCount;

	public Long getShiftId() {
		return shiftId;
	}
	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}
	public Long getCarwashSalesId() {
		return carwashSalesId;
	}
	public void setCarwashSalesId(Long carwashSalesId) {
		this.carwashSalesId = carwashSalesId;
	}
	public BigDecimal getNetSales() {
		return netSales;
	}
	public void setNetSales(BigDecimal netSales) {
		this.netSales = netSales;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getGrossSales() {
		return grossSales;
	}
	public void setGrossSales(BigDecimal grossSales) {
		this.grossSales = grossSales;
	}
	public BigDecimal getRefund() {
		return refund;
	}
	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}
	public Long getItemCount() {
		return itemCount;
	}
	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}
	public Long getRefundCount() {
		return refundCount;
	}
	public void setRefundCount(Long refundCount) {
		this.refundCount = refundCount;
	}
	public Long getNetCount() {
		return netCount;
	}
	public void setNetCount(Long netCount) {
		this.netCount = netCount;
	}
	public Long getDiscountCount() {
		return discountCount;
	}
	public void setDiscountCount(Long discountCount) {
		this.discountCount = discountCount;
	}
	
	
	
	
}
