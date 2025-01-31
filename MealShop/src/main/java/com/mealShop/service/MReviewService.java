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

	public void listReview(HashMap<String, Object> paramMap) {
		reviewDao.listReview(paramMap);
	}

	public void reviewDelete(HashMap<String, Object> paramMap) {
		reviewDao.reviewDelete(paramMap);
	}

	public void insertReview(HashMap<String, Object> paramMap) {
		reviewDao.insertReview(paramMap);
	}

	public int getUserOrderCnt(String userId, int pseq) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("pseq", pseq);
		
		return reviewDao.getUserOrderCnt(paramMap);
	}

	public int getUserReviewCnt(String userId, int pseq) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("pseq", pseq);
		
		return reviewDao.getUserReviewCnt(paramMap);
	}

}
