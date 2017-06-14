package com.ruanko.model;

import java.util.Date;

public class ConBusiModel {
	private int conId; 			
	private String conName; 	
	private Date drafTime;		
	
	public ConBusiModel() {
		this.conId = 0;
		this.conName = "";
		this.drafTime = new Date();
	}
	
	public int getConId() {
		return conId;
	}

	public void setConId(int conId) {
		this.conId = conId;
	}

	public String getConName() {
		return conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	public Date getDrafTime() {
		return drafTime;
	}

	public void setDrafTime(Date drafTime) {
		this.drafTime = drafTime;
	}
}
