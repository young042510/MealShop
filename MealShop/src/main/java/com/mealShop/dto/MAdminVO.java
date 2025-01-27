package com.mealShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class MAdminVO {

	private String id;
	private String pwd;
	private String name;
	private String phone;	
	
}
