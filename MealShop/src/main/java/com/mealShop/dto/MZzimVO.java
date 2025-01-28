package com.mealShop.dto;

import java.security.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MZzimVO {

	private int zseq;
	private int pseq;
	private String id;
	private Timestamp indate;
	private String name;
	private String image;
	private String price2;
	private String kind;
	private String result;
	
}
