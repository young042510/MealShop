package com.mealShop.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMAdminDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MAdminService {

	private IMAdminDao imAdminDao;

	public void getAdmin(HashMap<String, Object> paramMap) {
		imAdminDao.getAdmin(paramMap);
	}

	public ArrayList<HashMap<String, Object>> getBannerList(HashMap<String, Object> paramMap){
		return imAdminDao.bannerList(paramMap);
	}

	public void getAllcountAdmin(HashMap<String, Object> paramMap) {		
		imAdminDao.getAllcountAdmin(paramMap);
	}

	public void adminListQna(HashMap<String, Object> paramMap) {
		imAdminDao.adminListQna(paramMap);
	}

	public void admininsertQna(HashMap<String, Object> paramMap) {
		imAdminDao.admininsertQna(paramMap);
	}

	public void getAllcountAdminAsk(HashMap<String, Object> paramMap) {
		imAdminDao.getAllcountAdminAsk(paramMap);
	}

	public void adminListAsk(HashMap<String, Object> paramMap) {
		imAdminDao.adminListAsk(paramMap);
	}

	public void getAdminAsk(HashMap<String, Object> paramMap) {
		imAdminDao.getAdminAsk(paramMap);
	}

	public void adminAskReplyUpsert(HashMap<String, Object> paramMap) {
		imAdminDao.adminAskReplyUpsert(paramMap);
	}

	public void adminListReview(HashMap<String, Object> paramMap) {
		imAdminDao.adminListReview(paramMap);		
	}

	public void deleteReview(HashMap<String, Object> paramMap) {
		imAdminDao.deleteReview(paramMap);
	}

	public void adminnmListQna(HashMap<String, Object> paramMap) {
		imAdminDao.adminnmListQna(paramMap);
	}

	public void admininsertnmQna(HashMap<String, Object> paramMap) {
		imAdminDao.admininsertnmQna(paramMap);
	}

	public void getEventSelect(HashMap<String, Object> paramMap) {
		imAdminDao.getEventSelect(paramMap);
	}

	public void getImgesEvent(HashMap<String, Object> paramMap) {
		imAdminDao.getImgesEvent(paramMap);	
	}

	public void eventDelete(HashMap<String, Object> paramMap) {
		imAdminDao.eventDelete(paramMap);	
	}

	public void eventUpdate(HashMap<String, Object> paramMap) {
		imAdminDao.eventUpdate(paramMap);
	}

	public void eventInsert(HashMap<String, Object> paramMap) {
		imAdminDao.eventInsert(paramMap);	
	}

	public void deleteBanner() {
		imAdminDao.deleteBanner();
	}

	public void insertBanner(HashMap<String, Object> paramMap) {
		imAdminDao.insertBanner(paramMap);
	}

}
