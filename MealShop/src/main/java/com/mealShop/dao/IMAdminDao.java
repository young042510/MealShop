package com.mealShop.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mealShop.dto.MAdminVO;
import com.mealShop.dto.MProductVO;

@Mapper
public interface IMAdminDao {
	
	//관리자 로그인
	MAdminVO adminInfo(@Param("id") String id, @Param("pwd") String pwd);
	
	//상품관리 전체 목록 조회
	List<MProductVO> productList();
	
	//배너 조회
	ArrayList<HashMap<String, Object>> bannerList(HashMap<String, Object> paramMap);

	void getAllcountAdmin(HashMap<String, Object> paramMap);
	
}
