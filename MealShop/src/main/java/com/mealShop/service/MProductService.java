package com.mealShop.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mealShop.dao.IMProductDao;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MProductService {

	IMProductDao imProductDao;
	
	public void getNewBestProduct(HashMap<String, Object> paramMap) {
		imProductDao.getNewBestProduct(paramMap);
	}
	
	public void getAllCount(HashMap<String, Object> paramMap) {
		imProductDao.getAllCount(paramMap);
	}

	public void getalow(HashMap<String, Object> paramMap) {
		imProductDao.getalow(paramMap);
		
	}

	public void getahight(HashMap<String, Object> paramMap) {
		imProductDao.getahight(paramMap);
		
	}

	public void listProduct(HashMap<String, Object> paramMap) {
		imProductDao.listProduct(paramMap);
		
	}

	public void getBLow(HashMap<String, Object> paramMap) {
		imProductDao.getBlow(paramMap);
	}

	public void getBHight(HashMap<String, Object> paramMap) {
		imProductDao.getBHight(paramMap);
	}

	public void getBest(HashMap<String, Object> paramMap) {
		imProductDao.getBest(paramMap);
	}

	public void getNewLow(HashMap<String, Object> paramMap) {
		imProductDao.getNewLow(paramMap);
	}

	public void getNewHight(HashMap<String, Object> paramMap) {
		imProductDao.getNewHight(paramMap);
	}

	public void getNewList(HashMap<String, Object> paramMap) {
		imProductDao.getNewList(paramMap);
	}

	public void getLow(HashMap<String, Object> paramMap) {
		imProductDao.getLow(paramMap);
	}

	public void getHight(HashMap<String, Object> paramMap) {
		imProductDao.getHight(paramMap);
	}

	public void getKind(HashMap<String, Object> paramMap) {
		imProductDao.getKind(paramMap);
	}

	public void getProduct(HashMap<String, Object> paramMap) {
		imProductDao.getProduct(paramMap);
	}

	public void getImages(HashMap<String, Object> paramMap) {
		imProductDao.getImages(paramMap);		
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteProduct(HashMap<String, Object> paramMap) {
		imProductDao.deleteProduct(paramMap);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateProduct(HashMap<String, Object> paramMap) {
		imProductDao.updateProduct(paramMap);
		
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateProductUseyn(HashMap<String, Object> paramMap) {
		imProductDao.updateProductUseyn(paramMap);
	}
}
