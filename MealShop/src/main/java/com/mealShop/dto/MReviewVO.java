package com.mealShop.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MReviewVO {

	private int rseq;
	private int pseq;
	private String content;
	private String id;
	private Timestamp indate;
	private String pname;
	
}
