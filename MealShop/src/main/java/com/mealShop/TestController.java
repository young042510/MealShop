package com.mealShop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	@RequestMapping("/")
	public String main() {
		System.out.println("성공");
		return "test";
	}
}
