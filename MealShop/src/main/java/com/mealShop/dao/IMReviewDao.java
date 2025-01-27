package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMReviewDao {

	void getReviewListByPseq(HashMap<String, Object> paramMap);

	void getproductorderList(HashMap<String, Object> paramMap);

	
}
