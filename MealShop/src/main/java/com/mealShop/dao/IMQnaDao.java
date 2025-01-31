package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMQnaDao {

	void getQna(HashMap<String, Object> paramMap);

}
