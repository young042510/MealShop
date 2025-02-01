package com.mealShop.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMAdminDao {

	void getAdmin(HashMap<String, Object> paramMap);

	ArrayList<HashMap<String, Object>> bannerList(HashMap<String, Object> paramMap);

	void getAllcountAdmin(HashMap<String, Object> paramMap);

	void adminListQna(HashMap<String, Object> paramMap);

	void admininsertQna(HashMap<String, Object> paramMap);

	void getAllcountAdminAsk(HashMap<String, Object> paramMap);

	void adminListAsk(HashMap<String, Object> paramMap);

	void getAdminAsk(HashMap<String, Object> paramMap);

	void adminAskReplyUpsert(HashMap<String, Object> paramMap);

	void adminListReview(HashMap<String, Object> paramMap);

	void deleteReview(HashMap<String, Object> paramMap);

	void adminnmListQna(HashMap<String, Object> paramMap);

	void admininsertnmQna(HashMap<String, Object> paramMap);

	void getEventSelect(HashMap<String, Object> paramMap);

	void getImgesEvent(HashMap<String, Object> paramMap);

	void eventDelete(HashMap<String, Object> paramMap);

	void eventUpdate(HashMap<String, Object> paramMap);

	void eventInsert(HashMap<String, Object> paramMap);

	void deleteBanner();

	void insertBanner(HashMap<String, Object> paramMap);
	
}
