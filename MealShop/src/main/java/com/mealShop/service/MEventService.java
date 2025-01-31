package com.mealShop.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMEventDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MEventService {

	IMEventDao imEventDao;

	public void geteventList(HashMap<String, Object> paramMap) {
		imEventDao.geteventList(paramMap);
	}

	public void geteventOne(HashMap<String, Object> paramMap) {
		imEventDao.geteventOne(paramMap);
	}	
}
