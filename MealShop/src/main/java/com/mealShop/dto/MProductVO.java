package com.mealShop.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MProductVO {

	private int pseq;	
	private String name;
	private String kind;
	private int price1;
	private int price2;
	private String bestyn;
	private String content;
	private String useyn;
	private Date indate; //java util
	private String image;
	private int replycnt;
	
	private String image1;
	private String image2;
	
	public Map<String, Object> toParamMap() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", this.name);
        paramMap.put("kind", this.kind);
        paramMap.put("price1", this.price1);
        paramMap.put("price2", this.price2);
        paramMap.put("bestyn", this.bestyn);
        paramMap.put("useyn", this.useyn);
        paramMap.put("content", this.content);
        paramMap.put("image", this.image);
        paramMap.put("image1", this.image1);
        paramMap.put("image2", this.image2);
        return paramMap;
    }
}
