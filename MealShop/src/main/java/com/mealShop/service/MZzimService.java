package com.mealShop.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMZzimDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MZzimService  {
	
	IMZzimDao zzimDao;
	
	public void getlistZzim(HashMap<String, Object> paramMap) {
		zzimDao.getlistZzim(paramMap);
	}

	public void getZimcount(HashMap<String, Object> paramMap) {
		zzimDao.getZimcount(paramMap);
		
	}

	public void zzimInsert(HashMap<String, Object> paramMap) {
		zzimDao.zzimInsert(paramMap);
	}

	public void zzimDelete(HashMap<String, Object> paramMap) {
		zzimDao.zzimDelete(paramMap);
	}

	
}
