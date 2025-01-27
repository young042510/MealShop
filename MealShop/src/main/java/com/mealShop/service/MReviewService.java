package com.mealShop.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMReviewDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MReviewService {

	IMReviewDao reviewDao;

	public void getReviewListByPseq(HashMap<String, Object> paramMap) {
		reviewDao.getReviewListByPseq(paramMap);		
	}

	public void getproductorderList(HashMap<String, Object> paramMap) {
		reviewDao.getproductorderList(paramMap);
	}
	
	
}
