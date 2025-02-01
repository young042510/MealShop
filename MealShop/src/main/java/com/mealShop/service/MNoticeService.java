package com.mealShop.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMNoticeDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MNoticeService {
	
	IMNoticeDao imNoticeDao;

	public void getAllCountNotice(HashMap<String, Object> paramMap) {
		imNoticeDao.getAllCountNotice(paramMap);
	}

	public void getNoticeList(HashMap<String, Object> paramMap) {
		imNoticeDao.getNoticeList(paramMap);
	}

	public void getNoticeOne(HashMap<String, Object> paramMap) {
		imNoticeDao.getNoticeOne(paramMap);
	}

	public void getNoticeAll(HashMap<String, Object> paramMap) {
		imNoticeDao.getNoticeAll(paramMap);
	}

	public void insertNotice(HashMap<String, Object> paramMap) {
		imNoticeDao.insertNotice(paramMap);
	}

	public void updateNotice(HashMap<String, Object> paramMap) {
		imNoticeDao.updateNotice(paramMap);		
	}

	public void updateNoticeUseyn(HashMap<String, Object> paramMap) {
		imNoticeDao.updateNoticeUseyn(paramMap);
	}
	
}
