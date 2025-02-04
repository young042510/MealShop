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

import com.mealShop.dto.MMemberVO;
import com.mealShop.dto.MQnaVO;
import com.mealShop.dto.Paging;
import com.mealShop.service.MQnaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MQnaController {
		
	private final MQnaService qs;
	
	@GetMapping("/mypageForm")
	public String mypageForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser =
				(HashMap<String, Object>)session.getAttribute("loginUser");
		session.setAttribute("redirectUrl", "mypageForm");		//url 세션에 담기
		if(loginUser==null) {
			return "redirect:/userLogin";
		}else {
		
			return "mypage/mypage";
		}
	}
	
	@RequestMapping("/qnaForm")
	public ModelAndView qnaForm(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser =
				(HashMap<String, Object> )session.getAttribute("loginUser");
		session.setAttribute("redirectUrl", "qnaForm");		//url 세션에 담기
		if(loginUser ==null) {
			mav.setViewName("redirect:/userLogin");
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
		   qs.getAllCountQna(paramMap);
		    
		    int cnt = Integer.parseInt(paramMap.get("cnt").toString());
		    paging.setTotalCount(cnt);
		 paging.paging();
		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
			paramMap.put("id", loginUser.get("ID"));
			paramMap.put("ref_cursor", null);
			qs.listQna(paramMap);
			//System.out.println(cnt);
			
			ArrayList<HashMap<String, Object>> list
			 = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
			
			mav.addObject("paging", paging);
			mav.addObject("mqnaList", list);
			mav.setViewName("qna/qnaList");
					
		}
		
		return mav;
			
	}
	
	@RequestMapping("/qnaView")
	public ModelAndView qnaView(
			@RequestParam("qseq") int qseq ,HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser =
				(HashMap<String, Object> )session.getAttribute("loginUser");
		if(loginUser ==null) {
			mav.setViewName("redirect:/userLogin");
		}else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put("qseq", qseq);
			paramMap.put("ref_cursor", null);
			qs.getQna(paramMap);
			
			ArrayList<HashMap<String, Object>> list
			 =(ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("mqnaVO", list.get(0));
			mav.setViewName("qna/qnaView");
			
			
		}
		
		return mav;
	}
	
	@GetMapping("/qnaWriteForm")
	public String qnaWriteForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser
			= (HashMap<String, Object>)session.getAttribute("loginUser");
		
		if( loginUser == null) {
		 return "redirect:/userLogin";
		}else
			
		return "qna/qnaWrite";
	}
	
	@PostMapping("/qnaWrite")
	public ModelAndView qnaWrite(HttpServletRequest request, @ModelAttribute("dto")
	@Valid MQnaVO qnavo, BindingResult result,
	@RequestParam("subject") String subject, @RequestParam("content") String content) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser =
				(HashMap<String, Object> )session.getAttribute("loginUser");
		if(loginUser ==null) 
			mav.setViewName("redirect:/userLogin");
		else {
			if(result.getFieldError("subject")!=null) {
				mav.addObject("message", result.getFieldError("subject").getDefaultMessage());
				mav.setViewName("qna/qnaWrite");
				 return mav;
			}else if(result.getFieldError("content") !=null) {
				mav.addObject("message", result.getFieldError("content").getDefaultMessage());
				mav.setViewName("qna/qnaWrite");
				return mav;
		}
		
			
			
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("content", content);
			paramMap.put("subject", subject);
			paramMap.put("id", loginUser.get("ID"));
			qs.insertQna(paramMap);
			mav.setViewName("redirect:/qnaForm");
		}

		return mav;
	}
	
	@GetMapping("/nmqnaForm")
	public ModelAndView nomemberqnaForm(Model model, HttpServletRequest request,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "key", required = false) String key) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser =
				(HashMap<String, Object> )session.getAttribute("loginUser");
		if(loginUser!=null) {
			mav.setViewName("redirect:/qnaForm");
		}else {
			
			page=1;
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
		   qs.getAllCountnmQna(paramMap);
		    
		    int cnt = Integer.parseInt(paramMap.get("cnt").toString());
		    paging.setTotalCount(cnt);
		    paging.paging();
		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
			paramMap.put("ref_cursor", null);
			
			qs.nmlistQna(paramMap);
			
			ArrayList<HashMap<String, Object>> list
			 = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
			mav.addObject("paging", paging);
			mav.addObject("nmqnaList", list);
			mav.setViewName("qna/nmqnaList");			
		}
		return mav;	
	}
	
	@GetMapping("/nmqnaWriteForm")
	public String nmqnaWriteForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser
			= (HashMap<String, Object>)session.getAttribute("loginUser");
		if(loginUser!=null) {
			return "redirect:/qnaForm";
		}else {
		return "qna/nmqnaWrite";
		}
	}
		
	@PostMapping("/nmqnaWrite")
	public ModelAndView nmqnaWrite(HttpServletRequest request, @ModelAttribute("dto")
	@Valid MQnaVO qnavo, 
	BindingResult result,
	@RequestParam("subject") String subject, 
	@RequestParam("content") String content) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser =
				(HashMap<String, Object> )session.getAttribute("loginUser");
		if(loginUser!=null) {
			mav.setViewName("redirect:/qnaForm");
		}else{
			if(result.getFieldError("id")!=null) {
				mav.addObject("message", result.getFieldError("id").getDefaultMessage());
				mav.setViewName("qna/nmqnaWrite");
				 return mav;
			}else if(result.getFieldError("pwd")!=null) {
				mav.addObject("message", result.getFieldError("pwd").getDefaultMessage());
				mav.setViewName("qna/nmqnaWrite");
				 return mav;
			}else if(result.getFieldError("subject")!=null) {
				mav.addObject("message", result.getFieldError("subject").getDefaultMessage());
				mav.setViewName("qna/nmqnaWrite");
				 return mav;
			}else if(result.getFieldError("content") !=null) {
				mav.addObject("message", result.getFieldError("content").getDefaultMessage());
				mav.setViewName("qna/nmqnaWrite");
				return mav;
		}	
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("content", content);
			paramMap.put("subject", subject);
			paramMap.put("id", qnavo.getId());
			paramMap.put("pwd", qnavo.getPwd());
			
			qs.insertnmQna(paramMap);
			
			mav.setViewName("redirect:/nmqnaForm");
		}

		return mav;
	}
	
	@PostMapping("/nmqnaView")
	public ModelAndView nmqnaView(
			@RequestParam(value="nqseq",  required=false) int nqseq ,
			@RequestParam("pwd") String pwd ,
			@RequestParam("mvo") String mvo ,
			@RequestParam("checkpwd") String checkpwd ,
			MMemberVO membervo,
			@RequestParam(value="message",  required=false) String message ,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser =
				(HashMap<String, Object> )session.getAttribute("loginUser");
		if(loginUser!=null) {
			mav.setViewName("redirect:/qnaForm");
		}else {
			if(!checkpwd.equals(membervo.getPwd())) {
				mav.addObject("message", "비밀번호가 틀렸습니다");
				mav.setViewName("redirect:/pwdcheck");				
			}else{
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put("nqseq", nqseq);
			paramMap.put("ref_cursor", null);
			qs.getnmQna(paramMap);
			
			ArrayList<HashMap<String, Object>> list
			 =(ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("mqnaVO", list.get(0));
			mav.setViewName("qna/nmqnaView");
			}
			mav.addObject("nqseq", nqseq);
		}
		return mav;
	}
	
	@RequestMapping("/pwdcheck")
	public String pwdcheck(HttpServletRequest request,
			@RequestParam(value="message",  required=false) String message ,
			Model model,
			@RequestParam(value="checkpwd", required=false) String checkpwd ,
			@RequestParam(value="nqseq", required=false) int nqseq) {
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser
			= (HashMap<String, Object>)session.getAttribute("loginUser");
		if(loginUser!=null) {
			return "redirect:/qnaForm";
		}else {
			
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("nqseq", nqseq);
			paramMap.put("ref_cursor", null);
			
			qs.getNmqnaByNqseq(paramMap);
			
			ArrayList< HashMap<String, Object> > list 
			= (ArrayList< HashMap<String, Object>>)paramMap.get("ref_cursor");
			
			HashMap<String , Object > mvo = list.get(0);
			String pwd = (String)mvo.get("PWD");
			
			model.addAttribute("message", message);
			model.addAttribute("mvo", mvo);
			model.addAttribute("pwd", pwd);
			model.addAttribute("nqseq", nqseq);
			return "qna/pwdcheck";	
		}	
	}

}
