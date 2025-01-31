package com.mealShop.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MNoticeVO {

	private int nseq;
	private String subject;
	private String content;
	private Timestamp indate;
	private String image1;
	private String useyn;
	private String result;
	
}
