package com.mealShop.dto;

import java.security.Timestamp;

import lombok.Data;

@Data
public class MAskVO {

	private int aseq;
	private String title;
	private String content_a;
	private String id;
	private int pseq;
	private Timestamp indate_a;
	private int arseq;
	private String content_r;
	private Timestamp indate_r;
	private String pname;
	private String image;
	private int price2;
	
}
