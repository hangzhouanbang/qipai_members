package com.anbang.qipai.members.web.vo;

import java.math.BigDecimal;

public class GradeVo {

	private boolean success = true;

	private String msg;

	private Object data;

	private double shortage;

	private BigDecimal ProgressBar;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public double getShortage() {
		return shortage;
	}

	public void setShortage(double shortage) {
		this.shortage = shortage;
	}

	public BigDecimal getProgressBar() {
		return ProgressBar;
	}

	public void setProgressBar(BigDecimal progressBar) {
		ProgressBar = progressBar;
	}
}
