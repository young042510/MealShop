package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMOrderDao {
	
	//	주문하기
	void nowOrder(HashMap<String, Object> paramMap);
	
	// 전체 주문 조회 (admin)
	void listOrder(HashMap<String, Object> paramMap);
	
	// 상품등록
	void insertOrder(HashMap<String, Object> paramMap);
	
	// 상품주문리스트
	void listOrderByOseq(HashMap<String, Object> paramMap);
	
	// 상품주문리스트
	void listOrderByIdAll(HashMap<String, Object> paramMap);
	
	// 주문조회(회원)
	void selectOseqOrderAll(HashMap<String, Object> paramMap);

	// 주문조회 -회원탈퇴
	void deleteOrders(HashMap<String, Object> paramMap);

	// 주문상세조회 - 회원탈퇴
	void deleteOrder_detail(HashMap<String, Object> paramMap);
	
	// 주문/배송 정보 변경 (admin)
	void updateOrderResult(HashMap<String, Object> paramMap);

	// 주문취소 업데이트
	void orderCancelUpdate(HashMap<String, Object> paramMap);
	
	// 주문취소
	void orderCancelForm(HashMap<String, Object> paramMap);

	// void listOrderByIdIng(HashMap<String, Object> paramMap1);
	
	void getResultByOdseq(HashMap<String, Object> paramMap);

	// 즉시구매
	void orderInsertNow(HashMap<String, Object> paramMap);
}
