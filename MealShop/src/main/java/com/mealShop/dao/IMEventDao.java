package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMEventDao {

	void geteventList(HashMap<String, Object> paramMap);

	void geteventOne(HashMap<String, Object> paramMap);

}
