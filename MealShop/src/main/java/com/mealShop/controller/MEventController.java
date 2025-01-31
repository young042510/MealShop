package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.Paging;
import com.mealShop.service.MAdminService;
import com.mealShop.service.MEventService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class MEventController {

	MEventService eventService;
	MAdminService adminService;
	
	//세션 처리
	public void setPageAndKey(HttpServletRequest request
			, HttpSession session
			, HashMap<String, Object> paramMap
            , String tableName, String columnName) {

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
	
	//이벤트 목록 조회
	@GetMapping("/eventList")
	public ModelAndView eventList(HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView("customerCenter/eventList");
	    HttpSession session = request.getSession();
	    
	    HashMap<String, Object> paramMap = new HashMap<>();
	    setPageAndKey(request, session, paramMap, "mevent", "eseq");

	    Paging paging = new Paging();
	    paging.setPage((Integer) paramMap.get("page"));
	    paramMap.put("cnt", 0);

	    adminService.getAllcountAdmin(paramMap);
	    paging.setTotalCount((int) paramMap.get("cnt"));
	    mav.addObject("paging", paging);

	    paramMap.put("startNum", paging.getStartNum());
	    paramMap.put("endNum", paging.getEndNum());
	    paramMap.put("ref_cursor", null);

	    eventService.geteventList(paramMap);
	    ArrayList<HashMap<String, Object>> eventList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

	    mav.addObject("eventListVO", eventList);
	    mav.addObject("key", paramMap.get("key"));

	    return mav;
	}
	
	//이벤트 세부 정보 조회
	@GetMapping("/eventDetail")
	  public ModelAndView eventDetail(HttpServletRequest request
			  , @RequestParam("eseq") String eseq) {
		
	      ModelAndView mav = new ModelAndView();
	      HashMap<String, Object> paramMap = new HashMap<String, Object>();
	      paramMap.put("eseq", eseq);
	      
	      eventService.geteventOne( paramMap );
	      
	      ArrayList< HashMap<String, Object> >list = (ArrayList< HashMap<String, Object>>)paramMap.get("ref_cursor");
	      HashMap<String, Object> evo = list.get(0);
	      
	      mav.addObject("eventVO", evo);     
	      mav.setViewName("customerCenter/eventDetail");
	      
	      return mav;      
	  }
}
