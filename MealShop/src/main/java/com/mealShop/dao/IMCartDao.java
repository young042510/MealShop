package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMCartDao {
	
	// 장바구니 담기
	void insertCart(HashMap<String, Object> paramMap);
	// 장바구니 리스트
	void listCart(HashMap<String, Object> paramMap);
	// 장바구니 상품삭제
	void deleteCart(HashMap<String, Object> paramMap);
	// 회원탈되 장바구니 삭제처
	void deleteCart2(HashMap<String, Object> paramMap);
	// 장바구니 개수 카운트
	void getCartCnt(HashMap<String, Object> paramMap);

}