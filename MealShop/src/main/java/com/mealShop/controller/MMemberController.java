package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.MMemberVO;
import com.mealShop.service.MMemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MMemberController {

	@Autowired
	MMemberService ms;
	
	
	@RequestMapping("/userLogin")
	public String loginForm(HttpServletRequest request,  
		@RequestParam(value="message",required=false)String message) {
		request.setAttribute("message", message);
		return "member/login";
	}
	
	
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("loginUser");
		// 카트 개수 세션 삭제
		session.removeAttribute("cartCnt");
		session.removeAttribute("redirectUrl");
		return "redirect:/";
	}
	
	
	@RequestMapping(value="/contract")
	public String contract() {
		return "member/contract";
	}
	
	
	@RequestMapping("/joinForm")
	public String join_form( ) {
		return "member/join";
	}
	
	
	
	@RequestMapping("/idcheck")
	public ModelAndView idcheck( @RequestParam("id") String id ) {
		
		ModelAndView mav = new ModelAndView();
		
		HashMap<String , Object> paramMap = new HashMap<String , Object>();
		paramMap.put("id", id);
		paramMap.put("ref_cursor", null);
		ms.getMember( paramMap );
		ArrayList< HashMap<String , Object> > list 
			= ( ArrayList< HashMap<String , Object> > )paramMap.get("ref_cursor");
		
		//System.out.println(list.size());
		if( list.size() == 0 ) mav.addObject("result" , -1);
		else mav.addObject("result", 1);
		
		mav.addObject("id", id);
		mav.setViewName("member/idcheck");
		
		return mav;
	}
	
	
	
}