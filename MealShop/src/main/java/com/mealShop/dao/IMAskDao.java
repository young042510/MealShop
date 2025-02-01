package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMAskDao {

	void adminListAsk(HashMap<String, Object> paramMap);

}
