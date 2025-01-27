package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMProductDao {

	void getNewBestProduct(HashMap<String, Object> paramMap);
	
	void getAllCount(HashMap<String, Object> paramMap);

	void getalow(HashMap<String, Object> paramMap);

	void getahight(HashMap<String, Object> paramMap);

	void listProduct(HashMap<String, Object> paramMap);

	void getBlow(HashMap<String, Object> paramMap);

	void getBHight(HashMap<String, Object> paramMap);

	void getBest(HashMap<String, Object> paramMap);

	void getNewLow(HashMap<String, Object> paramMap);

	void getNewHight(HashMap<String, Object> paramMap);

	void getNewList(HashMap<String, Object> paramMap);

	void getLow(HashMap<String, Object> paramMap);

	void getHight(HashMap<String, Object> paramMap);

	void getKind(HashMap<String, Object> paramMap);

	void getProduct(HashMap<String, Object> paramMap);

	void getImages(HashMap<String, Object> paramMap);

	void deleteProduct(HashMap<String, Object> paramMap);

	void updateProduct(HashMap<String, Object> paramMap);

	void updateProductUseyn(HashMap<String, Object> paramMap);
}
