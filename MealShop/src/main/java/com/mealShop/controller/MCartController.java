package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.service.MCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MCartController {


	final private MCartService cs;
	
	// 1. 장바구니 담기 (수정)
	@RequestMapping(value="/cartInsert")
	@ResponseBody
	public Map<String,Object> cartInsert( HttpServletRequest request, Model model,
			@RequestParam(value="pseq",required = false)String pseq,
			@RequestParam(value="quantity",required = false)String quantity) {
		int cnt = 0;
		System.out.println("pseq:"+pseq+" / quantity:"+quantity);
		HttpSession session = request.getSession();
		session.setAttribute("redirectUrl", "productDetail?pseq="+pseq);		//url 세션에 담기
		HashMap<String, Object>loginUser
			= (HashMap<String, Object>)session.getAttribute("loginUser");
		
		HashMap<String, Object>paramMap = new HashMap<String, Object>();
		if( loginUser == null ) {
			paramMap.put("STATUS", 0);
			//return "STATUS : 0";
			return paramMap;
			//return "member/login";
		} else {
			
			paramMap.put("id", loginUser.get("ID") );
			paramMap.put("pseq", pseq);
			paramMap.put("quantity",quantity);
			cs.insertCart( paramMap );
			
			// 카트 개수 세션에 담기
			paramMap.put("cnt", 0);	// 카드 개수 담아올 변수
			cs.getCartCnt(paramMap);
			System.out.println(paramMap.get("cnt"));
			cnt = Integer.parseInt(paramMap.get("cnt").toString());
			session.setAttribute("cartCnt",cnt);
			
			paramMap.put("STATUS", 1);
		}
		return paramMap;
	}
	
	// 장바구니 리스트
	@GetMapping(value="/cartList")
	public ModelAndView cartList( HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.setAttribute("redirectUrl", "cartList");		//url 세션에 담기
		HashMap<String, Object> loginUser
			= (HashMap<String, Object>)session.getAttribute("loginUser");
		
		if( loginUser == null) {
			mav.setViewName("member/login");
		} else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", loginUser.get("ID") );
			paramMap.put("ref_cursor", null);
			cs.listCart(paramMap);
			ArrayList<HashMap<String, Object>> list
				= (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
			mav.addObject("cartList", list);
			
			int totalPrice = 0;
			for( HashMap<String, Object> cart : list ) {
				totalPrice += Integer.parseInt( cart.get("QUANTITY").toString() )
									* Integer.parseInt( cart.get("PRICE2").toString() ); 
			}
			mav.addObject("totalPrice", totalPrice);
			mav.setViewName("cart/cartList");
		}
		return mav;
	}
	
	// 장바구니 삭제
	@PostMapping(value="/cartDelete")
	public String cartDelete( HttpServletRequest request,
			@RequestParam("cseq") String [] cseqArr ) {
		int cnt = 0;
		HttpSession session = request.getSession();
		HashMap<String, Object>loginUser
			= (HashMap<String, Object>)session.getAttribute("loginUser");
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		for( String cseq : cseqArr ) {
			paramMap.put("cseq", cseq);
			cs.deleteCart(paramMap);
		}
		
		// 카트 개수 세션에 담기
		paramMap.put("id", loginUser.get("ID"));
		paramMap.put("cnt", 0);	// 카드 개수 담아올 변수
		cs.getCartCnt(paramMap);
		System.out.println(paramMap.get("cnt"));
		cnt = Integer.parseInt(paramMap.get("cnt").toString());
		session.setAttribute("cartCnt",cnt);
		
		return "redirect:/cartList";
	}
}