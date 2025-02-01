package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMNoticeDao {

	void getAllCountNotice(HashMap<String, Object> paramMap);

	void getNoticeList(HashMap<String, Object> paramMap);

	void getNoticeOne(HashMap<String, Object> paramMap);

	void getNoticeAll(HashMap<String, Object> paramMap);

	void insertNotice(HashMap<String, Object> paramMap);

	void updateNotice(HashMap<String, Object> paramMap);

	void updateNoticeUseyn(HashMap<String, Object> paramMap);

}
