package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.MAskVO;
import com.mealShop.dto.Paging;
import com.mealShop.service.MAskService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MAskController {
	
	private final MAskService mas;
	
	  @GetMapping("/askForm")
	  public ModelAndView askForm(Model model, HttpServletRequest request) {
		  ModelAndView mav =new ModelAndView();
		    HttpSession session = request.getSession();
			HashMap<String, Object> loginUser =
	     	(HashMap<String, Object>)session.getAttribute("loginUser");

			if(loginUser==null) {
				mav.setViewName("member/login");
				return mav;
			}else {
				
					int page=1;
					if(request.getParameter("sub") !=null) {
						session.removeAttribute("page");
					}
					if(request.getParameter("page")!=null) {
						page=Integer.parseInt(request.getParameter("page"));
						session.setAttribute("page", page);
					}else if(session.getAttribute("page")!=null) {
						page=(Integer)session.getAttribute("page");
					}else {
						session.removeAttribute("page");
					}	
				
				
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				Paging paging = new Paging();
				paging.setPage(page); //현재 페이지
				 paramMap.put("cnt" ,0);
			  mas.getAllCountAsk(paramMap);
				
				
				   int cnt = Integer.parseInt(paramMap.get("cnt").toString());
				    paging.setTotalCount(cnt);
				    paging.paging();
				    paramMap.put("startNum", paging.getStartNum());
				    paramMap.put("endNum", paging.getEndNum());
				
				paramMap.put("id", loginUser.get("ID"));
				//paramMap.put("pseq", pseq);
				paramMap.put("ref_cursor", null);
				mas.listMAsk(paramMap);
				System.out.println(cnt);
				ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
			
				mav.addObject("paging", paging);
				mav.addObject("myAskList", list);
				
				mav.setViewName("mypage/myAsk");
				
			}
				
		  return mav;
		  
		  
	  }
	  
	  @GetMapping("/askDetail")
	  public ModelAndView askDetail(@RequestParam("aseq") int aseq , Model model, HttpServletRequest request) {
		  ModelAndView mav = new ModelAndView();
		  HttpSession session = request.getSession();
				HashMap<String, Object> loginUser =
		     	(HashMap<String, Object>)session.getAttribute("loginUser");

				if(loginUser==null) {
					mav.setViewName("member/login");
					return mav;
					
				}else {
					HashMap<String, Object> paramMap = new HashMap<String, Object>();
				    paramMap.put("aseq", aseq);
				    paramMap.put("ref_cursor", null);
				    mas.getAsk(paramMap);
				    
				    ArrayList<HashMap<String, Object>> list 
				     =(ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
				    mav.addObject("maskVO", list.get(0));
				    mav.setViewName("mypage/askDetail");
				}
	  return mav;
	  }
			  
	  @GetMapping("/askWriteForm")
	  public String askWriteForm(HttpServletRequest request,
			  @RequestParam("pseq") int pseq) {
			HttpSession session = request.getSession();
			session.setAttribute("redirectUrl", "askWriteForm?pseq="+pseq);
			HashMap<String, Object> loginUser
				= (HashMap<String, Object>)session.getAttribute("loginUser");
			
			
			if( loginUser == null) {
			 return "member/login";
			}else {
				request.setAttribute("pseq",pseq);
				System.out.println("pseq" + pseq);
			}
			return "mypage/myAskWrite";
			
		}
	  
	  @PostMapping("/askWrite")
	  public ModelAndView askWrite(@ModelAttribute("dto") @Valid MAskVO askvo, BindingResult result,
			  @RequestParam("title") String title, @RequestParam("content_a") String content_a,
			  HttpServletRequest request ,  @RequestParam("pseq") String pseq
			  ) {
		  
		  //int pseq = Integer.parseInt(request.getParameter("pseq"));
		  
		  ModelAndView mav = new ModelAndView();
		  HttpSession session = request.getSession();
			HashMap<String, Object> loginUser =
					(HashMap<String, Object>)session.getAttribute("loginUser");

			if(loginUser==null) {
				mav.setViewName("member/login");
				return mav;
				
			}else {
				if(result.getFieldError("title") !=null) {
					mav.addObject("message", result.getFieldError("title").getDefaultMessage());
					mav.setViewName("mypage/myAskWrite");
					return mav;
				}else if(result.getFieldError("content_a") !=null) {
					mav.addObject("message", result.getFieldError("content_a").getDefaultMessage());
					mav.setViewName("mypage/myAskWrite");
					return mav;
				}
				
					HashMap<String, Object> paramMap = new HashMap<String , Object>();
					paramMap.put("id", loginUser.get("ID"));
					paramMap.put("title", title);
					paramMap.put("content_a", content_a);
					paramMap.put("pseq", pseq);
				
					mas.insertAsk(paramMap);
					mav.setViewName("redirect:/askForm");
			}
		  
		  return mav;
		  
		  
	  }
	   
	  
	}
