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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.AdminPaging;
import com.mealShop.dto.MAdminVO;
import com.mealShop.dto.MProductVO;
import com.mealShop.service.MAdminService;
import com.mealShop.service.MProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MAdminController {
	
	MAdminService adminService;
	MProductService productService;
	
	//관리자 로그인 페이지이동
	@GetMapping("admin")
	public String adminLoginForm() {
		return "admin/adminLogin";
	}
	
	//세션 처리
	public void setPageAndKey(HttpServletRequest request
			, HttpSession session
			, HashMap<String, Object> paramMap
            , String defaultTableName
            , String defaultColumnName) {
		
		String sub = request.getParameter("sub");
	    if ("y".equals(sub)) {
	        session.removeAttribute("key");
	        session.removeAttribute("page");
	    }

	    Integer page;
	    try {
	        page = (request.getParameter("page") != null)? Integer.parseInt(request.getParameter("page")) : null;
	    } catch (NumberFormatException e) {
	        page = null;
	    }

	    if (page == null) {
	        Object sessionPage = session.getAttribute("page");
	        if (sessionPage instanceof Integer) {
	            page = (Integer) sessionPage;
	        } else {
	            page = 1;
	        }
	    }

	    session.setAttribute("page", page);
	    paramMap.put("page", page);

	    String key = (request.getParameter("key") != null) ? request.getParameter("key")
	    												   : (session.getAttribute("key") instanceof String) ? (String) session.getAttribute("key") : "";
	    session.setAttribute("key", key);
	    paramMap.put("key", key);

	    paramMap.put("tableName", defaultTableName);
	    paramMap.put("columnName", defaultColumnName);
	}
	
	//관리자 로그인
	@PostMapping("/adminLogin")
	public String goAdminLogin(@RequestParam("id") String id
							  ,@RequestParam("pwd") String pwd
							  ,HttpSession session
							  ,Model model) {
		
		System.out.println("아이디>>>>" + id);
		System.out.println("비밀번호>>>>" + pwd);
		
		MAdminVO adminvo = adminService.login(id, pwd);
			    
		if (adminvo != null) { 
			session.setAttribute("loggedinAdmin", adminvo);
			System.out.println("로그인 성공!!");										
			return "redirect:/adminProductList";						
		} else {
			model.addAttribute("error", "아이디, 비밀번호가 틀렸습니다.");
			System.out.println("로그인 실패!!");
			return "admin/adminLogin";
		}
	}
	
	//관리자 로그아웃
	@GetMapping("/adminLogout")
	public String goAdminLogout(HttpSession session) {
		session.invalidate();
		System.out.println("로그아웃 성공!!");
		return "redirect:/admin";
	}
	
	//관리자 상품관리 페이지 상품목록
	@RequestMapping("/adminProductList")
	public ModelAndView adminProductList(HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView();
	    HttpSession session = request.getSession();

	    if (session.getAttribute("loggedinAdmin") == null) {
	        mav.setViewName("redirect:/admin");
	        return mav;
	    }

	    HashMap<String, Object> paramMap = new HashMap<>();
	    setPageAndKey(request, session, paramMap, "mproduct", "name");

	    AdminPaging paging = new AdminPaging();
	    paging.setPage((Integer) paramMap.get("page"));
	    paramMap.put("cnt", 0);
	    adminService.getAllcountAdmin(paramMap);

	    paging.setTotalCount((int) paramMap.get("cnt"));
	    mav.addObject("paging", paging);

	    paramMap.put("startNum", paging.getStartNum());
	    paramMap.put("endNum", paging.getEndNum());
	    paramMap.put("ref_cursor", null);
	    productService.listProduct(paramMap);

	    ArrayList<HashMap<String, Object>> productList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
	    mav.addObject("mproductList", productList);
	    mav.addObject("key", paramMap.get("key"));

	    mav.setViewName("admin/product/productList");
	    return mav;
	}
	
	//상품관리 수정
	@RequestMapping("/adminProductUpdateForm")
	public ModelAndView adminProductUpdateForm(HttpServletRequest request
			,@RequestParam("pseq") Integer pseq) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

	    if (session.getAttribute("loggedinAdmin") == null) {
	        mav.setViewName("redirect:/admin");
	        return mav;
	    }
	    
	    try {
	        HashMap<String, Object> paramMap = new HashMap<>();
	        paramMap.put("pseq", pseq);
	        paramMap.put("ref_cursor", null);
	        productService.getProduct(paramMap);

	        ArrayList<HashMap<String, Object>> mproductVOList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

	        HashMap<String, Object> productData = mproductVOList.get(0);
	        MProductVO pvo = new MProductVO();
	        
	        pvo.setPseq(Integer.parseInt(productData.get("PSEQ").toString()));
	        pvo.setKind(productData.get("KIND").toString());
	        pvo.setName(productData.get("NAME").toString());
	        pvo.setBestyn((String) productData.get("BESTYN"));
	        pvo.setUseyn((String) productData.get("USEYN"));
	        pvo.setContent(productData.get("CONTENT").toString());
	        pvo.setPrice1(Integer.parseInt(productData.get("PRICE1").toString()));
	        pvo.setPrice2(Integer.parseInt(productData.get("PRICE2").toString()));
	        pvo.setImage(productData.get("IMAGE").toString());

	        paramMap.put("ref_cursor_Image1", null);
	        paramMap.put("ref_cursor_Image2", null);
	        productService.getImages(paramMap);

	        ArrayList<HashMap<String, Object>> image1List = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor_Image1");
	        ArrayList<HashMap<String, Object>> image2List = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor_Image2");

	        if (!image1List.isEmpty()) {pvo.setImage1((String) image1List.get(0).get("IMAGE"));}
	        if (!image2List.isEmpty()) {pvo.setImage2((String) image2List.get(0).get("IMAGE"));}

	        mav.addObject("pvo", pvo);
	        mav.setViewName("admin/product/productUpdateForm");

	    } catch (Exception e) {
	        mav.addObject("error", "상품 정보 수정 중 오류 발생!!!");
	        mav.setViewName("redirect:/adminProductList");
	    }
		return mav;
	}
	
	//상품관리 삭제
	@RequestMapping("/adminProductDelete")
	public ModelAndView adminProductDelete(HttpServletRequest request,
			@RequestParam("pseq")int pseq) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		if (session.getAttribute("loggedinAdmin") == null) {
			mav.setViewName("redirect:/admin");
		} else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pseq", pseq);
			productService.deleteProduct(paramMap);		
			mav.setViewName("redirect:/adminProductList");
		}
		
		System.out.println("삭제할 상품번호???" + pseq);
		return mav;
	}
	
	//상품관리 상품정보 업데이트
	@RequestMapping(value = "adminProductUpdate", method = RequestMethod.POST)
	public ModelAndView adminProductUpdate(HttpServletRequest request
			,@ModelAttribute("pvo") @Valid MProductVO pvo
			,BindingResult result) {
		
	    ModelAndView mav = new ModelAndView();
	    HttpSession session = request.getSession();

	    Object loggedinAdmin = session.getAttribute("loggedinAdmin");

	    if (loggedinAdmin == null) {
	        mav.setViewName("redirect:/admin");
	        return mav;
	    }

	    if (result.hasErrors()) {
	        String errorMessage = result.getFieldError() != null ? result.getFieldError().getDefaultMessage() : ">>>>>>>>실패";
	        System.out.println("errorMessage>>>>>>>>>>>>>>>>>>>>>>>" + errorMessage);
	        mav.addObject("message", errorMessage);
	        mav.setViewName("admin/product/productUpdateForm");
	        return mav;
	    }

	    try {
	        HashMap<String, Object> paramMap = new HashMap<>();
	        paramMap.put("pseq", pvo.getPseq());
	        paramMap.put("kind", pvo.getKind());
	        paramMap.put("name", pvo.getName());
	        paramMap.put("bestyn", pvo.getBestyn());
	        paramMap.put("useyn", pvo.getUseyn());
	        paramMap.put("content", pvo.getContent());
	        paramMap.put("price1", pvo.getPrice1());
	        paramMap.put("price2", pvo.getPrice2());
	        paramMap.put("image", pvo.getImage());
	        paramMap.put("image1", pvo.getImage1());
	        paramMap.put("image2", pvo.getImage2());

	        productService.updateProduct(paramMap);
	        
	        mav.setViewName("redirect:/adminProductList");   
	        
	    } catch (Exception e) {
	        mav.addObject("message", "상품 업데이트 중 오류 발생! 다시 시도해주세요!");
	        mav.setViewName("admin/product/productUpdateForm");
	    }
	    return mav;
	}
	
	//상품관리 상품등록
	@RequestMapping("/adminProductSave")
	public ModelAndView adminProductSave(HttpServletRequest request
			, @RequestParam("selectedIndex") String selectedIndex
			, @RequestParam("checkBox_pseq") String [] pseqArr) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

	    Object loggedinAdmin = session.getAttribute("loggedinAdmin");
	    HashMap<String, Object> paramMap = new HashMap<>();

	    if (loggedinAdmin == null) {
	        mav.setViewName("redirect:/admin");
	        return mav;
	        
	    }else {
	    	for (String pseq : pseqArr) {
	            paramMap.put("pseq", Integer.parseInt(pseq));
	            paramMap.put("selectedIndex", selectedIndex);
	            productService.updateProductUseyn(paramMap);
	        }
		}
	    mav.setViewName("redirect:/adminProductList");
		return mav;
	}
	
}
