package com.mealShop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealShop.dao.IMAdminDao;
import com.mealShop.dto.MAdminVO;
import com.mealShop.dto.MProductVO;

@Service
public class MAdminService {

	@Autowired
	private IMAdminDao imAdminDao;
	
	//관리자 로그인
	public MAdminVO login(String id, String pwd) {
		return imAdminDao.adminInfo(id, pwd);
	}

	//상품 정보 조회
	public List<MProductVO> getProductList() {
		return imAdminDao.productList();
	}
	
	//배너 리스트 조회
	public ArrayList<HashMap<String, Object>> getBannerList(HashMap<String, Object> paramMap){
		return imAdminDao.bannerList(paramMap);
	}
	
	//
	//
	public void getAllcountAdmin(HashMap<String, Object> paramMap) {		
		imAdminDao.getAllcountAdmin(paramMap);
	}

}
