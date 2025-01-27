package com.mealShop.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MProductVO {

	private int pseq;	
	private String name;
	private String kind;
	private int price1;
	private int price2;
	private String bestyn;
	private String content;
	private String useyn;
	private Date indate; //java util
	private String image;
	private int replycnt;
	
	private String image1;
	private String image2;
}
