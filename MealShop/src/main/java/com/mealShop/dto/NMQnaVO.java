package com.mealShop.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class NMQnaVO {
	
	private int nqseq;
	private String subject;
	private String content;
	private String reply;
	private String id;
	private String pwd;
	private String rep;
	private Timestamp indate;
}
