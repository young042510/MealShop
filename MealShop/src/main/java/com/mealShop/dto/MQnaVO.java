package com.mealShop.dto;

import java.security.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQnaVO {

	private int qseq;
	private String subject;
	private String content;
	private String reply;
	private String id;
	private String pwd;
	private String rep;
	private Timestamp indate;
	
}
