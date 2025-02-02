package com.mealShop.service;

import org.springframework.stereotype.Service;

import com.mealShop.dao.IMAskDao;


@Service
public class MAskService {

	@Autowired
	IMAskDao madao;

	public void listMAsk(HashMap<String, Object> paramMap) {
		madao.listMask(paramMap);
	}

	public void getAsk(HashMap<String, Object> paramMap) {
	madao.getAsk(paramMap);
	}

	public void getAllCountAsk(HashMap<String, Object> paramMap) {
	 madao.getAllCountAsk(paramMap);
	}

	@Transactional(rollbackFor = Exception.class)
	public void insertAsk(HashMap<String, Object> paramMap) {
		madao.insertAsk(paramMap);
	}
	
	
	
}