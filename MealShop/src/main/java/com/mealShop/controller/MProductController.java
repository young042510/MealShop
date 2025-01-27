package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.Paging;
import com.mealShop.service.MAdminService;
import com.mealShop.service.MProductService;
import com.mealShop.service.MReviewService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@SessionAttributes({"loginUser","key","page","subMenu"})
public class MProductController {
	
	MAdminService adminService;
	MProductService productService;
	MReviewService reviewService;
	
	@ModelAttribute("loginUser")
	public HashMap<String, Object> initLoginUser(){
		return new HashMap<>();
	}
	
	@ModelAttribute("key")
	public String initKey(){
		return "";
	}
	
	@ModelAttribute("page")
	public String initPage(){
		return "1";
	}
	
	private HashMap<String, Object> getLoginUser(@ModelAttribute("loginUser") HashMap<String, Object> loginUser){
		if (loginUser != null) {return loginUser;}
		return loginUser;
	}
	
	//메인 홈 - 로그인 세션 필요하면 추가
	@GetMapping({"/webMain","/"}) 
	public ModelAndView goMain(HttpServletRequest request, Model model) throws Exception {
				
		ModelAndView mav = new ModelAndView("index");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("ref_cursor1", null);
		paramMap.put("ref_cursor2", null);
		
		productService.getNewBestProduct(paramMap);
		
		ArrayList<HashMap<String, Object>> newList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor1");
		ArrayList<HashMap<String, Object>> bestList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor2");
		
		paramMap.put("ref_cursor", null);
				
		adminService.getBannerList(paramMap);
		
		ArrayList<HashMap<String, Object>> bannerList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
		
		mav.addObject("newList", newList);
		mav.addObject("bestList", bestList);
		mav.addObject("bannerList", bannerList);

		return mav;
	}

	//메인홈 상품 전체 조회 ----가격 높은순일때 useyn 칼럼이 n이면 조회됨 수정해야됨
	@RequestMapping("/productAllForm")
	public String productAllForm(Model model
			, @RequestParam(value = "sort", defaultValue = "recently") String sort
			, @RequestParam(value = "idx", defaultValue = "0") Integer idx
			, @RequestParam(value = "sub", required = false) String sub
			, @RequestParam(value = "key", required = false) String requestKey
			, @RequestParam(value = "page", required = false) Integer requestPage
			, @ModelAttribute("key") String key
			, @ModelAttribute("page") Integer page) {
		
		if ("y".equals(sub)) {key = ""; page = 1;}

		if (requestKey != null) {key = requestKey;}
		if (requestPage != null) {page = requestPage;}
		
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", key);
        paramMap.put("cnt", 0);
        
        productService.getAllCount(paramMap);

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setTotalCount((int)paramMap.get("cnt"));
        
        model.addAttribute("paging", paging);

        paramMap.put("startNum", paging.getStartNum());
        paramMap.put("endNum", paging.getEndNum());
        paramMap.put("ref_cursor", null);
        
        ArrayList<HashMap<String, Object>> productList;
        
        switch (sort) {
	        case "low_price":
	            productService.getalow(paramMap);
	            break;
	        case "high_price":
	            productService.getahight(paramMap);
	            break;
	        default:
	            productService.listProduct(paramMap);
        }
        
	    productList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
	
	    model.addAttribute("mproductVoList", productList);
	    model.addAttribute("key", key);
	    model.addAttribute("idx", idx);
	    
	    System.out.println("키가 뭐야???????????????"+key);
        
		return "product/productAll";
	}
	
	//상단 카테고리별 상품 조회
	@RequestMapping("/productForm")
	public String productForm(HttpServletRequest request
			, Model model
			,@RequestParam(value = "sort", defaultValue = "recently") String sort
			,@RequestParam(value = "idx", defaultValue = "0") Integer idx
            ,@RequestParam(value = "sub", required = false) String sub
            ,@RequestParam(value = "key", required = false) String requestKey
            ,@RequestParam(value = "page", required = false) Integer requestPage
            ,@ModelAttribute("key") String key
            ,@ModelAttribute("page") Integer page) {
		
		if ("y".equals(sub)) {key = ""; page = 1;}
		
		if (requestKey != null) {key = requestKey;}
		if (requestPage != null) {page = requestPage;}
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("key", key);
        paramMap.put("cnt", 0);
		
		String kind = request.getParameter("kind") != null ? request.getParameter("kind") : "";
		String bestyn = request.getParameter("bestyn") != null ? request.getParameter("bestyn") : "n";
		String newyn = request.getParameter("newyn") != null ? request.getParameter("newyn") : "n";
		
		Paging paging = new Paging();
		paging.setPage(page);
        paging.setTotalCount((int)paramMap.get("cnt"));      
        model.addAttribute("paging", paging);
        
        paramMap.put("startNum", paging.getStartNum());
        paramMap.put("endNum", paging.getEndNum());       
		paramMap.put("kind", kind.isEmpty() ? "" : kind);
		paramMap.put("key", key.isEmpty() ? "" : key);
		paramMap.put("bestyn", "y".equals(bestyn) ? "y" : null);
		paramMap.put("newyn", "y".equals(newyn) ? "y" : null);

		System.out.println("paramMap: " + paramMap);
	    System.out.println("Paging startNum: " + paging.getStartNum());
	    System.out.println("Paging endNum: " + paging.getEndNum());

		ArrayList<HashMap<String, Object>> productList = new ArrayList<>();
		paramMap.put("ref_cursor", null);
		
		if ("y".equals(paramMap.get("bestyn"))) {
	        switch (sort) {
	            case "low_price":
	            	productService.getBLow(paramMap);
	                break;
	            case "high_price":
	                productService.getBHight(paramMap);
	                break;
	            default:
	                productService.getBest(paramMap);
	                break;
	        }
	    } else if ("y".equals(paramMap.get("newyn"))) {
	        switch (sort) {
	            case "low_price":
	                productService.getNewLow(paramMap);
	                break;
	            case "high_price":
	                productService.getNewHight(paramMap);
	                break;
	            default:
	                productService.getNewList(paramMap);
	                break;
	        }
	    } else {
	        switch (sort) {
	            case "low_price":
	                productService.getLow(paramMap);
	                break;
	            case "high_price":
	                productService.getHight(paramMap);
	                break;
	            default:
	                productService.getKind(paramMap);
	                break;
	        }
	    }
		
		if (paramMap.get("ref_cursor") instanceof ArrayList) {productList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");}
		
		model.addAttribute("mproductVoList", productList);
	    model.addAttribute("key", key);
	    model.addAttribute("idx", idx);
	    model.addAttribute("kind", kind);
	    model.addAttribute("bestyn", bestyn);
	    model.addAttribute("newyn", newyn);
	    model.addAttribute("sort", sort);

		return "product/productKind";
	}
	
	//상품 개별 디테일 조회
	@RequestMapping("/productDetail")
	public ModelAndView productDetail(HttpServletRequest request
			, Model model
			, @RequestParam("pseq") int pseq
			, @ModelAttribute("loginUser") HashMap<String , Object> loginUser) {
		
		ModelAndView mav = new ModelAndView();	
		mav.setViewName("product/productDetail");
		
		//장바구니, 찜하기 등등 기능에 필요해서 로그인 세션 확인 추가했는데 나중에 수정
		//loginUser = getLoginUser(loginUser);
		//System.out.print("로그인 누구?????>>>>" + loginUser);
		
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("pseq", pseq);
		paramMap.put("ref_cursor", null);		
		productService.getProduct(paramMap);
		
		ArrayList<HashMap<String,Object>> mproductVOList = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
	    HashMap<String,Object> resultMap = mproductVOList.get(0);
	    mav.addObject("mproductVO", resultMap);
	    
	    paramMap.put("ref_cursor_Image1", null);
	    paramMap.put("ref_cursor_Image2", null);
	    productService.getImages(paramMap);
	    
		ArrayList<HashMap<String,Object>> image1List = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor_Image1");
		ArrayList<HashMap<String,Object>> image2List = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor_Image2");
		mav.addObject("mpdimg", image1List );
		mav.addObject("mpdimg2", image2List );

		paramMap.put("ref_cursor_getReview", null);
		reviewService.getReviewListByPseq(paramMap);		
		ArrayList<HashMap<String,Object>> reViewList = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor_getReview");
		mav.addObject("mreview", reViewList );
		paramMap.put("ref_cursor_productorderList", null);
		reviewService.getproductorderList(paramMap);
		ArrayList<HashMap<String,Object>> productorderList = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor_productorderList");
		if(productorderList.size() ==0)model.addAttribute("reviewresult",-1);
		else model.addAttribute("reviewresult",1);
		model.addAttribute("pseq",pseq);
        mav.addObject("productorderList", productorderList);
        
		//로그인되면 > 찜 갯수 구현
        
		return mav;
	}
	
	//로그인되면 찜확인
	//로그인되면 찜삭제
	//로그인되면 찜등록
	
}
