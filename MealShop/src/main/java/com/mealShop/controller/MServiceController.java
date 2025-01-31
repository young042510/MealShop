package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.Paging;
import com.mealShop.service.MAdminService;
import com.mealShop.service.MNoticeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class MServiceController {

	MAdminService adminService;
	MNoticeService noticeService;
	
	//서비스 소개
	@GetMapping(value = "/company")
	public String company() {
		return "service/company";
	}
	
	//이용 약관
	@GetMapping(value = "/agreement")
	public String agreement() {
		return "service/agreement";
	}
	
	//개인정보 처리 방침
	@GetMapping(value = "/privacy")
	public String privacy() {
		return "service/privacy";
	}
	
	//공지 목록 조회
	@GetMapping("/noticeList")
	public ModelAndView noticeList(HttpServletRequest request) {
		
	    ModelAndView mav = new ModelAndView("customerCenter/noticeList");
	    HttpSession session = request.getSession();

	    HashMap<String, Object> paramMap = new HashMap<>();
	    setPageAndKey(request, session, paramMap, "mnotice", "nseq");

	    Paging paging = new Paging();
	    paging.setPage((Integer) paramMap.get("page"));
	    paramMap.put("cnt", 0);

	    noticeService.getAllCountNotice(paramMap);
	    paging.setTotalCount((int) paramMap.get("cnt"));
	    mav.addObject("paging", paging);

	    paramMap.put("startNum", paging.getStartNum());
	    paramMap.put("endNum", paging.getEndNum());
	    paramMap.put("ref_cursor", null);

	    noticeService.getNoticeList(paramMap);
	    ArrayList<HashMap<String, Object>> noticeList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

	    mav.addObject("noticeList", noticeList);
	    mav.addObject("key", paramMap.get("key"));

	    return mav;
	}
	
	public void setPageAndKey(HttpServletRequest request, HttpSession session, 
		            HashMap<String, Object> paramMap, String tableName, String columnName) {

		String key = request.getParameter("key");
		Integer page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : null;

		if ("y".equals(request.getParameter("sub"))) {
		session.removeAttribute("key");
		session.removeAttribute("page");
		}
		
		key = key != null ? key : (String) session.getAttribute("key");
		page = page != null ? page : (session.getAttribute("page") != null ? (Integer) session.getAttribute("page") : 1);
		
		session.setAttribute("key", key);
		session.setAttribute("page", page);
		
		paramMap.put("key", key != null ? key : "");
		paramMap.put("page", page);
		paramMap.put("tableName", tableName);
		paramMap.put("columnName", columnName);
		
	}
	
	//공지 상세 조회
	@GetMapping("noticeDetail")
	  public ModelAndView noticeDetail(HttpServletRequest request
			  , @RequestParam("nseq") String nseq) {
		
	      ModelAndView mav = new ModelAndView("customerCenter/noticeDetail");
	      HashMap<String, Object> paramMap = new HashMap<String, Object>();

	      paramMap.put("nseq", nseq);
	      
	      noticeService.getNoticeOne( paramMap );
	      
	      ArrayList< HashMap<String, Object> >list = (ArrayList< HashMap<String, Object>>)paramMap.get("ref_cursor");
	      HashMap<String, Object> nvo = list.get(0);
	      
	      mav.addObject("noticeVO", nvo);     
	      return mav;
	  }
}
