package com.gsd.pos.model;

import java.math.BigDecimal;
import java.util.Date;

public class FuelInventory {
	private Long shiftId;
	private String grade;
	private String gradeName;
	private BigDecimal volume;
	private Long tankId;
	private Date datetime;
	public Long getShiftId() {
		return shiftId;
	}
	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
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
	public Long getTankId() {
		return tankId;
	}
	public void setTankId(Long tankId) {
		this.tankId = tankId;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}


}
