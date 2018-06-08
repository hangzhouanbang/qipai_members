package com.anbang.qipai.members.cqrs.q.dbo;

import org.springframework.data.mongodb.core.mapping.Document;

import com.dml.accounting.AccountingSummary;

@Document
public class GoldAccountingSummary implements AccountingSummary {

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
