package com.mealShop.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMReviewDao;

import lombok.AllArgsConstructor;

@Service
public class MReviewService {
	
	@Autowired
	IMReviewDao reviewDao;

	public void listQna(HashMap<String, Object> paramMap) {
		qdao.listQna(paramMap);
	}

	public void getQna(HashMap<String, Object> paramMap) {
		qdao.getQna(paramMap);
		
	}
	@Transactional(rollbackFor = Exception.class)
	public void insertQna(HashMap<String, Object> paramMap) {
		qdao.insertQna(paramMap);
	}

	public void getAllCountQna(HashMap<String, Object> paramMap) {
		qdao.getAllCount(paramMap);
	}

	public void getAllCountnmQna(HashMap<String, Object> paramMap) {
		qdao.getAllCountnmQna(paramMap);
	}

	public void nmlistQna(HashMap<String, Object> paramMap) {
		qdao.nmlistQna(paramMap);
	}

	public void insertnmQna(HashMap<String, Object> paramMap) {
		qdao.insertnmQna(paramMap);
	}

	public void getnmQna(HashMap<String, Object> paramMap) {
		qdao.getnmQna(paramMap);
	}

	public void getNmqnaByNqseq(HashMap<String, Object> paramMap) {
		qdao.getNmqnaByNqseq(paramMap);
	}
	public int getUserReviewCnt(String userId, int pseq) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("pseq", pseq);
		
		return reviewDao.getUserReviewCnt(paramMap);
	}

}
