package com.mealShop.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMQnaDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MQnaService {
	
	private IMQnaDao imQnaDao;

	public void getQna(HashMap<String, Object> paramMap) {
		imQnaDao.getQna(paramMap);	
	}

}
