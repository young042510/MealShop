package com.mealShop.dto;



import java.security.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MOrderVo {
	
	private Integer pseq; 
	
	@NotNull(message = "상품명을 입력하세요")
	private String name; // 상품명
	
	@NotBlank(message = "상품분류를 입력하세요")
	private String kind; // 상품분류
	
	@NotNull(message = "원가를 입력하세요")
	private int price1; // 원가
	
	@NotNull(message = "판매가를 입력히세요")
	private int price2; // 판매가
	 
	private String bestyn;  // 베스트상품
	private String content;  // 상품 내용
	private String useyn;  // 재고확보 (판매/재고확보중)
	
	@NotBlank(message = "썸네일 이미지를 입력하세요")
	private String image; // 썸네일 이미지
	private Timestamp indate; // 등록일
	private int replycnt; // 상품관련 댓글 수
	
	// 상세이미지 임시저장 -> mpdimg, mpdimg2
	private String image1;
	private String image2;
	
}
