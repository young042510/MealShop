package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMZzimDao {

	void getlistZzim(HashMap<String, Object> paramMap);

	void getZimcount(HashMap<String, Object> paramMap);

	void zzimInsert(HashMap<String, Object> paramMap);

	void zzimDelete(HashMap<String, Object> paramMap);
	
}
