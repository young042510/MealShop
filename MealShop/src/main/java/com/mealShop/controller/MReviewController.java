package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.MReviewVO;
import com.mealShop.service.MReviewService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class MReviewController {

	MReviewService reviewService;

	private HashMap<String, Object> getLoginUser(HttpServletRequest request){		
		HttpSession session = request.getSession();
		return (HashMap<String, Object>) session.getAttribute("loginUser");
	}
	
	//리뷰 목록 조회
	@GetMapping("/reviewForm")
	public ModelAndView riviewForm(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap =new HashMap<String, Object>();
		
		HashMap<String , Object> loginUser = getLoginUser(request);
		
		if (loginUser == null) {
			mav.setViewName("member/login");
		} else {
			paramMap.put("id", loginUser.get("ID"));
			paramMap.put("ref_cursor", null);
			reviewService.listReview(paramMap);
			
			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
			
			mav.addObject("myReviewList", list);
			mav.setViewName("mypage/myReview");
		}	
		return mav;
	}
	
	//리뷰 삭제
	@GetMapping("myReviewDelete")
	public ModelAndView myReviewDeltet(HttpServletRequest request
			, @RequestParam("rseq") int rseq) {

		ModelAndView mav = new ModelAndView();
		HashMap<String , Object> loginUser = getLoginUser(request);
	
		if (loginUser == null) {
			mav.setViewName("member/login");
			return mav;
		}else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("rseq" , rseq);
			reviewService.reviewDelete(paramMap);
			
			mav.setViewName("redirect:/reviewForm");
		}
		return mav;
	}
	
	//리뷰 작성 폼
	@GetMapping("/reviewWriteForm") 
	public ModelAndView reviewWriteForm(HttpServletRequest request
			, @RequestParam("pseq") int pseq) {
		
		 ModelAndView mav =new ModelAndView();
		 HashMap<String , Object> loginUser = getLoginUser(request);
		
		if(loginUser == null) {
			mav.setViewName("member/login");
		}else {		
			request.setAttribute("pseq", pseq);	
			mav.addObject("pseq", pseq);
			mav.setViewName("mypage/myReviewWrite");
		}
	return mav;
	}
	
	//리뷰 작성
	@PostMapping("/reviewWrite")
	public ModelAndView reviewWrite(HttpServletRequest request
			, @RequestParam("pseq") int pseq 
			, @RequestParam("content") String content
			, @ModelAttribute("dto") @Valid MReviewVO mreviewvo
			, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String , Object> loginUser = getLoginUser(request);
		
		if (loginUser == null) {
			
			mav.setViewName("member/login");
			return mav;
		}	
		
		if (result.hasFieldErrors("content")) {
		
			mav.addObject("message", result.getFieldError("content").getDefaultMessage());
			mav.addObject("pseq",pseq);
			mav.setViewName("mypage/myReviewWrite");
			return mav;
		}
		
		HashMap<String, Object> paramMap =  new HashMap<String, Object>();
		 
		paramMap.put("id", loginUser.get("ID"));
		paramMap.put("content", content);
		paramMap.put("pseq", pseq);
		
		reviewService.insertReview(paramMap);
		
		mav.addObject("pseq",pseq);
		mav.setViewName("redirect:/productDetail");
		
		return mav;
	}
	
}
