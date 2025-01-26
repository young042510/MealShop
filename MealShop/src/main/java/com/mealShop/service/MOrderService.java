package com.mealShop.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mealShop.dao.IMOrderDao;

import lombok.RequiredArgsConstructor;

@Service
public class MOrderService {

	@Autowired
	IMOrderDao odao;

	public void nowOrder(HashMap<String, Object> paramMap) {
		odao.nowOrder(paramMap);

	}

	public void listOrder(HashMap<String, Object> paramMap) {
		odao.listOrder(paramMap);
	}

	@Transactional(rollbackFor = Exception.class)
	public void insertOrder(HashMap<String, Object> paramMap) {
		odao.insertOrder(paramMap);
	}

	public void listOrderByIdAll(HashMap<String, Object> paramMap1) {
		odao.listOrderByIdAll(paramMap1);
	}

	public void listOrderByOseq(HashMap<String, Object> paramMap2) {
		odao.listOrderByOseq(paramMap2);
	}

	public void selectOseqOrderAll(HashMap<String, Object> paramMap) {
		odao.selectOseqOrderAll(paramMap);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteOrders(HashMap<String, Object> paramMap) {
		odao.deleteOrders(paramMap);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteOrder_detail(HashMap<String, Object> paramMap) {
		odao.deleteOrder_detail(paramMap);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateOrderResult(HashMap<String, Object> paramMap) {
		odao.updateOrderResult(paramMap);
	}

	public void orderCancelUpdate(HashMap<String, Object> paramMap) {
		odao.orderCancelUpdate(paramMap);
	}

	public void orderCancelForm(HashMap<String, Object> paramMap) {
		odao.orderCancelForm(paramMap);
	}

	/*
	 * public void listOrderByIdIng(HashMap<String, Object> paramMap1) {
	 * odao.listOrderByIdIng( paramMap1 ); }
	 */
	
	public void getResultByOdseq(HashMap<String, Object> paramMap) {
		odao.getResultByOdseq(paramMap);

	}

	public void orderInsertNow(HashMap<String, Object> paramMap) {
		odao.orderInsertNow(paramMap);

	}
}
