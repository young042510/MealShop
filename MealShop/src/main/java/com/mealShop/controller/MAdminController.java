package com.mealShop.controller;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.AdminPaging;
import com.mealShop.dto.MAdminVO;
import com.mealShop.dto.MAskVO;
import com.mealShop.dto.MEventVO;
import com.mealShop.dto.MNoticeVO;
import com.mealShop.dto.MProductVO;
import com.mealShop.dto.MQnaVO;
import com.mealShop.dto.NMQnaVO;
import com.mealShop.service.MAdminService;
import com.mealShop.service.MAskService;
import com.mealShop.service.MEventService;
import com.mealShop.service.MMemberService;
import com.mealShop.service.MNoticeService;
import com.mealShop.service.MOrderService;
import com.mealShop.service.MProductService;
import com.mealShop.service.MQnaService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MAdminController {
	
	MAdminService adminService;
	MProductService productService;
	MOrderService orderService;
	MMemberService memberService;
	MQnaService qnaService;
	MAskService askService;
	MNoticeService noticeService;
	MEventService eventService;
	
	private final ServletContext context;
	
	//관리자 로그인 페이지이동
	@GetMapping("/admin")
	public String adminLoginForm(@SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {	
		if (loginAdmin == null) {
			return "admin/adminLogin";
		}else {
			return "redirect:/adminProductList";
		}
	}
	
	//관리자 로그인
	@PostMapping("/adminLogin")
	public ModelAndView goAdminLogin(HttpServletRequest request
							  , @ModelAttribute("avo") @Valid MAdminVO adminvo
							  , BindingResult result) {
	
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/adminLogin");
		
		if (result.hasErrors()) {
			mav.addObject("error", "아이디와 비밀번호를 입력해주세요.");
			return mav;
		}
		
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", adminvo.getId());
		paramMap.put("ref_cursor", null);
		adminService.getAdmin(paramMap);
		
		ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

		if (list.isEmpty()) {
			mav.addObject("error", "존재하지 않는 관리자ID 입니다.");
			return mav;
		}
		
		HashMap<String, Object> resultMap = list.get(0);
		
		if (!adminvo.getPwd().equals(resultMap.get("PWD"))) {
			mav.addObject("error", "비밀번호가 틀렸습니다.");
			return mav;
		}

		MAdminVO admin = new MAdminVO();
		admin.setId((String) resultMap.get("ID"));
	    admin.setName((String) resultMap.get("NAME"));
		
		HttpSession session = request.getSession();
		session.setAttribute("loggedinAdmin", admin);
		
		mav.setViewName("redirect:/adminProductList");		
		
		return mav;
		
	}
	
	//관리자 로그아웃
	@GetMapping("/adminLogout")
	public String adminLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/admin";
	}
	
	//sub 파라미터가 y일때 기존 세션 pag, key 값 초기화
	//요청 request에 page와 key가 있으면 사용, 없으면 세션에 저장된 값을 사용함!
	public void setPageAndKey(HttpServletRequest request
			, HttpSession session
			, HashMap<String, Object> paramMap) {
		
		String sub = request.getParameter("sub");
		
		if ("y".equals(sub)) {
			session.removeAttribute("key");
			session.removeAttribute("page");
		}
		
		Integer page = 1;

		if (request.getParameter("page") != null) {
	        try {
	            page = Integer.parseInt(request.getParameter("page"));
	        } catch (NumberFormatException e) {
	            page = 1;
	        }
	    }

		session.setAttribute("page", page);
		paramMap.put("page", page);

		String key = request.getParameter("key");

	    if (key == null || key.trim().isEmpty()) {
	        key = "";
	        session.removeAttribute("key");
	    } else {
	        session.setAttribute("key", key);
	    }
		paramMap.put("key", key);	
	}
		
	//판매상품관리 상품목록 조회 - 검색기능O
	@RequestMapping("/adminProductList")
	public ModelAndView adminProductList(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {
		
	    ModelAndView mav = new ModelAndView();
	    HttpSession session = request.getSession();

	    if (loginAdmin == null) {return new ModelAndView("redirect:/admin");}

	    HashMap<String, Object> paramMap = new HashMap<>();
	    
	    setPageAndKey(request, session, paramMap);
	    
	    paramMap.put("cnt", 0);
	    paramMap.put("tableName", "mproduct");
	    paramMap.put("columnName", "name");
	    
	    AdminPaging paging = new AdminPaging();
	    paging.setPage((Integer) paramMap.get("page"));

	    adminService.getAllcountAdmin(paramMap);
	    paging.setTotalCount((int) paramMap.get("cnt"));
	    mav.addObject("paging", paging);

	    paramMap.put("startNum", paging.getStartNum());
	    paramMap.put("endNum", paging.getEndNum());
	    paramMap.put("ref_cursor", null);
	    
	    productService.fullListProduct(paramMap);

	    ArrayList<HashMap<String, Object>> productList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
	    mav.addObject("mproductList", productList);
	    mav.addObject("key", paramMap.get("key"));

	    mav.setViewName("admin/product/productList");
	    
	    return mav;
	}
	
	//판매상품관리 상품수정 폼
	@RequestMapping("/adminProductUpdateForm")
	public ModelAndView adminProductUpdateForm(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @RequestParam("pseq") Integer pseq) {
		
		ModelAndView mav = new ModelAndView();

		if (loginAdmin == null) {return new ModelAndView("redirect:/admin");}
	    
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
	
	//판매상품관리 상품삭제
	@RequestMapping("/adminProductDelete")
	public ModelAndView adminProductDelete(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @RequestParam("pseq")int pseq) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			paramMap.put("pseq", pseq);
			productService.deleteProduct(paramMap);		
			mav.setViewName("redirect:/adminProductList");
		}
		return mav;
	}
	
	//판매상품관리 상품정보 업데이트
	@PostMapping("/adminProductUpdate")
	public ModelAndView adminProductUpdate(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @ModelAttribute("pvo") @Valid MProductVO pvo
			, BindingResult result) {
		
	    ModelAndView mav = new ModelAndView();

	    if (loginAdmin == null) {return new ModelAndView("redirect:/admin");}

	    if (result.hasErrors()) {
	        String errorMessage = result.getFieldError() != null ? result.getFieldError().getDefaultMessage() : "상품 정보 업데이트 실패!!!";
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
	
	//판매상품관리 상품수정, 저장
	@RequestMapping("/adminProductSave")
	public ModelAndView adminProductSave(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @RequestParam("selectedIndex") String selectedIndex
			, @RequestParam("checkBox_pseq") String [] pseqArr) {
		
		ModelAndView mav = new ModelAndView();
	    HashMap<String, Object> paramMap = new HashMap<>();

	    if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
	    }else {
	    	for (String pseq : pseqArr) {
	            paramMap.put("pseq", Integer.parseInt(pseq));
	            paramMap.put("selectedIndex", selectedIndex);
	            productService.updateProductUseyn(paramMap);
	        }
	    	mav.setViewName("redirect:/adminProductList");
		}
		return mav;
	}
	
	//판매상품관리 상품등록 폼
	@PostMapping("/adminProductWriteForm")
	public ModelAndView adminProductWriteForm(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		
		if(loginAdmin == null ) {
			mav.setViewName("redirect:/admin");
		}else {
			mav.setViewName("admin/product/productWriteForm");
		}
		return mav;
	}
	
	//판매상품관리 상품등록
	@PostMapping("/adminProductWrite")
	public ModelAndView adminProductWrite(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @ModelAttribute("pvo") @Valid MProductVO pvo
			, BindingResult result) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/product/priductWriteForm");

		if (result.hasErrors()) {
			result.getFieldErrors().forEach(error -> mav.addObject("message", error.getDefaultMessage()));
		}
		
		if (loginAdmin == null) {return new ModelAndView("redirect:/admin");}
		
		productService.insertProduct(pvo.toParamMap());
		mav.setViewName("redirect:/adminProductList");
		
		return mav;
	}
	
	//상품등록 시 이미지 업로드
	@PostMapping("/fileup")
	public ResponseEntity<Map<String, Object>> fileUpload(@RequestParam(value = "image", required = false) MultipartFile image
			, @RequestParam(value = "image1", required = false) MultipartFile image1
			, @RequestParam(value = "image2", required = false) MultipartFile image2) {
		
		String savePath = context.getRealPath("/images");
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			
			String filename = saveFile(image, savePath);
			String filename1 = saveFile(image1, savePath);
			String filename2 = saveFile(image2, savePath);
			
			resultMap.put("STATUS", 1);
			resultMap.put("FILENAME", filename);
			resultMap.put("FILENAME1", filename1);
			resultMap.put("FILENAME2", filename2);
			
			return ResponseEntity.ok(resultMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("STATUS", 0, "MESSAGE", "파일 업로드 실패!!"));
		}
		
	}

	//상품등록 시 이미지 파일 저장
	private String saveFile(MultipartFile file, String savePath) throws IOException{
		if (file != null && !file.isEmpty()) {
            File saveDir = new File(savePath);
            if (!saveDir.exists()) saveDir.mkdirs();

            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
            File dest = new File(savePath, uniqueFilename);
            file.transferTo(dest);

            return uniqueFilename;
        }
		return null;
	}
	
	//주문배송관리 목록 조회 - 검색기능O
	@RequestMapping("/adminOrderList")
	public ModelAndView getMethodName(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		}else {
			setPageAndKey(request, session, paramMap);
			paramMap.put("tableName", "morder_view");
			paramMap.put("columnName", "oseq");
			paramMap.put("cnt", 0);

			AdminPaging paging = new AdminPaging();
		    paging.setPage((Integer) paramMap.get("page"));
			
		    adminService.getAllcountAdmin(paramMap);
		    paging.setTotalCount((int) paramMap.get("cnt"));
		    mav.addObject("paging", paging);

		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
		    paramMap.put("ref_cursor", null);

		    orderService.listOrder(paramMap);
		    ArrayList<HashMap<String, Object>> orderList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

		    mav.addObject("morderList", orderList);
		    mav.addObject("key", paramMap.get("key"));

		    mav.setViewName("admin/order/morderList");
		    
		}//if
		return mav;
	}
	
	//주문배송관리 주문상태 변경 저장
	@PostMapping("/adminOrderSave")
	public ModelAndView adminOrderSave(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @RequestParam("selectedIndex") int selectedIndex
			, @RequestParam("result") String [] adminSeqArr) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) { 
			mav.setViewName("redirect:/admin");
		} else {
			for (String odseq : adminSeqArr) {
				paramMap.put("odseq", Integer.parseInt(odseq));
				paramMap.put("selectedIndex", selectedIndex);
				orderService.updateOrderResult(paramMap);
			}
			mav.setViewName("redirect:/adminOrderList");
		}//if
		return mav;
	}
	
	//회원관리 목록 조회 - 검색기능O
	@RequestMapping("/adminMemberList")
	public ModelAndView adminMemberList(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (loginAdmin == null) { 
			mav.setViewName("redirect:/admin");
			return mav;
		} else {
			
			setPageAndKey(request, session, paramMap);
			
			paramMap.put("cnt", 0);
			paramMap.put("tableName", "mmember");
			paramMap.put("columnName", "name");
			
			AdminPaging paging = new AdminPaging();
		    paging.setPage((Integer) paramMap.get("page"));
			
		    adminService.getAllcountAdmin(paramMap);
		    paging.setTotalCount((int) paramMap.get("cnt"));
		    mav.addObject("paging", paging);

		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
		    paramMap.put("ref_cursor", null);
		    
		    memberService.listMember(paramMap);
		    
		    ArrayList<HashMap<String, Object>> memberList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");

		    mav.addObject("memberList", memberList);
		    mav.addObject("key", paramMap.get("key"));
		    
			mav.setViewName("admin/member/mmemberList");
			
			return mav;
		}
	}
	
	//회원관리 활성체크 항목 변경 저장
	@RequestMapping("/adminMemberSave")
	public ModelAndView adminMemberSave(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @RequestParam("selectedIndex") String seletedIndex
			, @RequestParam("useyn") String [] idArr) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) { 
			mav.setViewName("redirect:/admin");
			return mav;
		} else {
			for (String id : idArr) {
				paramMap.put("id", id);
				paramMap.put("selectedIndex", seletedIndex);
				memberService.updateMemberResult(paramMap);
			}
			mav.setViewName("redirect:/adminMemberList");
			return mav;
		}
	}
	
	//고객서비스 관리 Q&A 목록 조회 - 검색기능O
	@RequestMapping("/adminQnaList")
	public ModelAndView adminQnaList(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) { 
			mav.setViewName("redirect:/admin");
		} else {
			
			setPageAndKey(request, session, paramMap);
			
			paramMap.put("cnt", 0);
			paramMap.put("tableName", "mqna");
			paramMap.put("columnName", "subject");
			paramMap.put("columnName", "id");
			
			AdminPaging paging = new AdminPaging();
		    paging.setPage((Integer) paramMap.get("page"));
			
		    adminService.getAllcountAdmin(paramMap);
		    paging.setTotalCount((int) paramMap.get("cnt"));
		    paging.paging();
		    mav.addObject("paging", paging);

		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
		    paramMap.put("ref_cursor_qna", null);
		    
		    adminService.adminListQna(paramMap);
		    
		    ArrayList<HashMap<String, Object>> qnaList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor_qna");
		   
			mav.addObject("mqnaList", qnaList);
			mav.addObject("key", paramMap.get("key"));
			
			mav.setViewName("admin/qna/qnaList");
		}
		return mav;
	}
	
	//고객서비스 관리 Q&A 상세 조회
	@GetMapping("/adminQnaDetail")
	public ModelAndView adminQnaDetail(HttpServletRequest request
			, @RequestParam("qseq") int qseq
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {

		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) { 
			mav.setViewName("redirect:/admin");
		} else {
			paramMap.put("qseq", qseq);
	        paramMap.put("ref_cursor", null);
		
	        qnaService.getQna(paramMap);
	        
	        ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
	        
	        mav.addObject("mqnaVO", list.get(0));
	        mav.setViewName("admin/qna/qnaDetail");
		}
		return mav;
	}
	
	//고객서비스 관리 Q&A 상세 답변 수정
	@RequestMapping("/adminQnaRepSave")
	public ModelAndView adminQnaRepSave(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @RequestParam("qseq") String qseq
			, @RequestParam("reply") String reply
			, @ModelAttribute("qvo") @Valid MQnaVO qvo
			, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) {return new ModelAndView("redirect:/admin");} 
		
		if (result.hasFieldErrors("reply")) {
	        mav.addObject("message", result.getFieldError("reply").getDefaultMessage());
	        mav.addObject("qseq", qseq);
	        mav.setViewName("admin/qna/qnaDetail");
	        return mav;
	    }
	    paramMap.put("qseq", qvo.getQseq());
	    paramMap.put("reply", qvo.getReply());
	    
	    adminService.admininsertQna(paramMap);
	    mav.addObject("qseq", qseq);
	    mav.setViewName("redirect:/adminQnaDetail");
	    
	    return mav;
	}
	
	//고객서비스 관리 상품문의 조회
	@RequestMapping("/adminAskForm")
	public ModelAndView adminAskForm(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView("admin/qna/askList");
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) {return new ModelAndView("redirect:/admin");}
		
		setPageAndKey(request, session, paramMap);
		
		paramMap.put("cnt", 0);
		paramMap.put("tableName", "ask_view");
		paramMap.put("columnName", "pname");
		paramMap.put("columnName", "content_a");
		
		AdminPaging paging = new AdminPaging();
	    paging.setPage((Integer) paramMap.get("page"));
	    
	    adminService.getAllcountAdminAsk(paramMap);
	    
	    paging.setTotalCount((int) paramMap.get("cnt"));
	    paging.paging();
	    mav.addObject("paging", paging);

	    paramMap.put("startNum", paging.getStartNum());
	    paramMap.put("endNum", paging.getEndNum());
	    paramMap.put("ref_cursor_ask", null);

	    adminService.adminListAsk(paramMap);

	    ArrayList<HashMap<String, Object>> askList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor_ask");

	    mav.addObject("askList", askList);
	    mav.addObject("key", paramMap.get("key"));

	    return mav;
	}
	
	//고객서비스 관리 상품문의 상세 조회
	@GetMapping("/adminAskDetail")
	public ModelAndView adminAskDetail(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @RequestParam("aseq") int aseq) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (loginAdmin == null) {
	        mav.setViewName("admin/adminLogin");
	    } else {
	    	paramMap.put("aseq", aseq);
	    	paramMap.put("ref_cursor", null);
	    	adminService.getAdminAsk(paramMap);
	    	
	    	ArrayList<HashMap<String, Object>> getAdminAsk = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			mav.addObject("dto", getAdminAsk.get(0));
			mav.setViewName("admin/qna/askDetail");
		}
		return mav;
	}
	
	//고객서비스 관리 상품문의 답변 저장, 수정
	@PostMapping({"/adminAskRepSave", "/adminAskUpdate"})
	public String adminAskReplySave(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin
			, @ModelAttribute("dto") @Valid MAskVO maskvo
			, @RequestParam("aseq") int aseq
	        , @RequestParam("content_r") String content_r
	        , BindingResult result
	        , Model model) {

	    if (loginAdmin == null) {return "admin/adminLogin";}

	    if (result.getFieldError("content_r") != null || content_r == null || content_r.trim().isEmpty()) {
	        model.addAttribute("message", "답글을 작성해주세요");
	        model.addAttribute("aseq", aseq);
	        return "admin/qna/askDetail";
	    }

	    HashMap<String, Object> paramMap = new HashMap<>();
	    
	    paramMap.put("aseq", aseq);
	    paramMap.put("content", content_r);

	    adminService.adminAskReplyUpsert(paramMap);

	    return "redirect:/adminAskDetail?aseq=" + aseq;
	}
	
	//고객서비스 관리 상품후기 목록 조회
	@RequestMapping("/adminReviewForm")
	public ModelAndView adminReviewFrom(HttpServletRequest request
			, @SessionAttribute(name="loggedinAdmin", required = false) MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView("admin/qna/reviewList");
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (loginAdmin == null) {
			mav.setViewName("admin/adminLogin");
		} else {
			setPageAndKey(request, session, paramMap);
			
			paramMap.put("cnt", 0);
			paramMap.put("tableName", "mreview_view");
			paramMap.put("columnName", "name");
			
			adminService.getAllcountAdmin(paramMap);

		    AdminPaging paging = new AdminPaging();
		    paging.setPage((Integer) paramMap.get("page"));

		    int cnt = Integer.parseInt(paramMap.get("cnt").toString());
		    paging.setTotalCount(cnt);
		    paging.paging();
		    
		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
		    
		    paramMap.put("ref_cursor_review", null);
			adminService.adminListReview(paramMap);
			
			ArrayList<HashMap<String, Object>> reviewList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor_review");
			
			mav.addObject("paging", paging);
			mav.addObject("reviewList", reviewList);
			mav.addObject("key", paramMap.get("key"));
			mav.setViewName("admin/qna/reviewList");
		}
		return mav;
	}
	
	//고객서비스 관리 상품후기 삭제
	@RequestMapping("/adminReviewDelete")
	public ModelAndView adminReviewDelete(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (loginAdmin == null) {
			mav.setViewName("admin/adminLogin");
		} else {
			String [] rseqArr = request.getParameterValues("rseq");
			for (String rseq : rseqArr) {
				paramMap.put("rseq", rseq);
				adminService.deleteReview(paramMap);
			}
			mav.setViewName("redirect:/adminReviewForm");
		}
		return mav;
	}
	
	//고객서비스 관리 비회원관리 목록 조회
	@RequestMapping("/adminnmQnaList")
	public ModelAndView adminnmQnaList(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (loginAdmin ==null) {
			mav.setViewName("admin/adminLogin");
		} else {
			setPageAndKey(request, session, paramMap);
			
			paramMap.put("cnt", 0);
			paramMap.put("tableName", "nmqna");
			paramMap.put("columnName", "subject");
			paramMap.put("columnName", "id");
			
			adminService.getAllcountAdmin(paramMap);
			
			AdminPaging paging = new AdminPaging();
		    paging.setPage((Integer) paramMap.get("page"));

		    int cnt = Integer.parseInt(paramMap.get("cnt").toString());
		    paging.setTotalCount(cnt);
		    paging.paging();
		    
		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
		    
		    paramMap.put("ref_cursor_qna", null);
			adminService.adminnmListQna(paramMap);
			
			ArrayList<HashMap<String, Object>> nmqnaList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor_qna");
		
			mav.addObject("paging", paging);
			mav.addObject("nmqnaList", nmqnaList);
			
			mav.setViewName("admin/nonmember/nmqnaList");
		}
		return mav;
	}
	
	//고객서비스 관리 비회원관리 상세 조회
	@GetMapping("/adminnmQnaDetail")
	public ModelAndView adminnmQnaDetail(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @RequestParam("nqseq") int nqseq) {

	      ModelAndView mav = new ModelAndView();
	      HashMap<String, Object> paramMap = new HashMap<String, Object>();
	      
	      if (loginAdmin == null) {
	         mav.setViewName("admin/adminLogin");
	      } else {
	         paramMap.put("nqseq", nqseq);
	         paramMap.put("ref_cursor", null);
	         
	         qnaService.getnmQna(paramMap);

	         ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
	         
	         mav.addObject("nmqnaVO", list.get(0));
	         mav.setViewName("admin/nonmember/nmqnaDetail");
	      }
	      return mav;
	   }
	
	//고객서비스 관리 비회원관리 답변 저장
	@RequestMapping("/adminnmQnaRepSave")
	public ModelAndView admin_nmqna_repSave(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @RequestParam("nqseq") String nqseq
			, @RequestParam("reply") String reply
			, @ModelAttribute("qvo") @Valid NMQnaVO qvo
			, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) {
			mav.setViewName("admin/adminLogin");
		} else if (result.getFieldError("reply") != null) {
			mav.addObject("message", result.getFieldError("reply").getDefaultMessage());
			mav.addObject("nqseq", nqseq);
			mav.setViewName("admin/nonmember/nmqnaDetail");
		}

		paramMap.put("nqseq", qvo.getNqseq());
		paramMap.put("reply", qvo.getReply());
		
		adminService.admininsertnmQna(paramMap);

		mav.addObject("nqseq", nqseq);
		mav.setViewName("redirect:/adminnmQnaDetail");

		return mav;
	}
	
	//공지사항 목록 조회
	@RequestMapping("/adminNoticeList")
	public ModelAndView adminNoticeList(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<>();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			setPageAndKey(request, session, paramMap);
			
			paramMap.put("cnt", 0);
			paramMap.put("tableName", "notice");
	        paramMap.put("columnName", "subject");
			
	        adminService.getAllcountAdmin(paramMap);
	        
	        AdminPaging paging = new AdminPaging();
		    paging.setPage((Integer) paramMap.get("page"));

		    int cnt = Integer.parseInt(paramMap.get("cnt").toString());
		    paging.setTotalCount(cnt);
		    mav.addObject("paging", paging);
		    
		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
		    
		    paramMap.put("ref_cursor", null);
		    
		    noticeService.getNoticeAll(paramMap);
	          
		    ArrayList<HashMap<String, Object>> noticeList = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
	        mav.addObject("noticeList", noticeList);
	        mav.addObject("key", paramMap.get("key"));

	        mav.setViewName("admin/customerCenter/adminNoticeList");
		}
		
		return mav;
	}
	
	//공지사항 목록 상세 조회
	@GetMapping("adminNoticeDetail")
	public ModelAndView adminNoticeDetail(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin 
			, @RequestParam("nseq") int nseq) {
		
		ModelAndView mav = new ModelAndView();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("nseq", nseq);
			paramMap.put("ref_cursor", null);

			noticeService.getNoticeOne(paramMap);

			ArrayList<HashMap<String, Object>> MNoticeVOList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			
			HashMap<String, Object> resultMap = MNoticeVOList.get(0);
			
			MNoticeVO nvo = new MNoticeVO();
			
			nvo.setNseq(nseq);
			nvo.setSubject(resultMap.get("SUBJECT").toString());
			nvo.setIndate((Timestamp)resultMap.get("INDATE"));
			nvo.setUseyn((String) resultMap.get("USEYN"));
			nvo.setContent(resultMap.get("CONTENT").toString());
			nvo.setImage1((String) resultMap.get("IMAGE1"));
			nvo.setResult((String) resultMap.get("RESULT"));
			
			mav.addObject("nvo", nvo);
			
			mav.setViewName("admin/customerCenter/adminNoticeDetail");
		}
		return mav;
	}
	
	//공지사항 새 공지 작성폼
	@PostMapping("/adminNoticeInsertForm")
	public ModelAndView adminNoticeInsertForm(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			mav.setViewName("admin/customerCenter/adminNoticeInsert");
		}
		return mav;
	}
	
	//공지사항 새 공지 작성 후 저장
	@RequestMapping("/adminNoticeInsert")
	public ModelAndView adminNoticeInsert(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @ModelAttribute("nvo") @Valid MNoticeVO nvo
			, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/customerCenter/adminNoticeInsert");
		
		if(result.getFieldError("subject")!=null) {
			System.out.println("subject:" + nvo.getSubject() + "/ content:" + nvo.getContent());
			mav.addObject("message", result.getFieldError("subject").getDefaultMessage());
			return mav;
		}else if(result.getFieldError("content")!=null) {
			System.out.println("subject:" + nvo.getSubject() + "/ content:" + nvo.getContent());
			mav.addObject("message", result.getFieldError("content").getDefaultMessage());
			return mav;
		}
		
		if (loginAdmin == null) {
			mav.setViewName("admin/adminLogin");
			return mav;
		} else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("subject", nvo.getSubject());
			paramMap.put("useyn", nvo.getUseyn());
			paramMap.put("content", nvo.getContent());
			paramMap.put("image1", nvo.getImage1());
			paramMap.put("result", nvo.getResult());
			
			noticeService.insertNotice(paramMap);
			
			mav.setViewName("redirect:/adminNoticeList");
		}
		return mav;
	}
	
	//공지사항 수정 후 저장
	@RequestMapping("adminNoticeUpdate")
	public ModelAndView adminNoticeUpdate(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @ModelAttribute("nvo") @Valid MNoticeVO nvo
			, BindingResult result
			, @RequestParam(value="nseq",required=false) Integer nseq) {
		
		ModelAndView mav = new ModelAndView();

		mav.setViewName("admin/customerCenter/adminNoticeDetail");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if(result.getFieldError("subject")!=null) {
			mav.addObject("message", result.getFieldError("subject").getDefaultMessage());
			return mav;
		}else if(result.getFieldError("content")!=null) {
			mav.addObject("message", result.getFieldError("content").getDefaultMessage());
			return mav;
		}
		
		if (loginAdmin == null) {
			mav.setViewName("admin/adminLogin");
			return mav;
		} else {
			paramMap.put("nseq", nseq);
			paramMap.put("subject", nvo.getSubject());
			paramMap.put("useyn", nvo.getUseyn());
			paramMap.put("content", nvo.getContent());
			paramMap.put("image1", nvo.getImage1());
			paramMap.put("result", nvo.getResult());

			noticeService.updateNotice(paramMap);
			
			mav.setViewName("redirect:/adminNoticeList");
		}
		return mav;
	}
	
	//공지사항 게시상태 수정 후 저장
	@PostMapping("adminNoticeSave")
	public ModelAndView adminNoticeSave(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @RequestParam("selectedIndex") String selectedIndex
			, @RequestParam("checkBox_nseq") String [] nseqArr) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			for(String nseq : nseqArr) {
				paramMap.put("nseq", Integer.parseInt( nseq ));
				paramMap.put("selectedIndex", selectedIndex);
				noticeService.updateNoticeUseyn(paramMap);
			}
			mav.setViewName("redirect:/adminNoticeList");
		}
		return mav;
	}
	
	//이벤트 목록 조회
	@RequestMapping("/adminEventList")
	public ModelAndView adminEventList(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			
			setPageAndKey(request, session, paramMap); 
			
			paramMap.put("cnt", 0);
			paramMap.put("tableName", "mevent");
			paramMap.put("columnName", "title");
			
			AdminPaging paging = new AdminPaging();
		    paging.setPage((Integer) paramMap.get("page"));

		    int cnt = Integer.parseInt(paramMap.get("cnt").toString());
		    paging.setTotalCount(cnt);
		    mav.addObject("paging", paging);
		    
		    paramMap.put("startNum", paging.getStartNum());
		    paramMap.put("endNum", paging.getEndNum());
		    
		    paramMap.put("ref_cursor", null);
		    
			adminService.getAllcountAdmin(paramMap);

			paging.setTotalCount((int) paramMap.get("cnt"));
			
			paramMap.put("startNum",paging.getStartNum());
			paramMap.put("endNum",paging.getEndNum());
			paramMap.put("ref_cursor", null);
			
			eventService.geteventList(paramMap);
			
			ArrayList<HashMap<String, Object>> eventList = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor");
			
			mav.addObject("eventListVO", eventList);
			mav.addObject("key", paramMap.get("key"));
			
			mav.setViewName("admin/customerCenter/adminEventList");
		}
		return mav;
	}
	
	//이벤트 목록 상세조회
	@GetMapping("/adminEventDetail")
	  public ModelAndView admineventdetail(HttpServletRequest request
			  , @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			  , @RequestParam("eseq") int eseq) {
		
		ModelAndView mav = new ModelAndView();
		  
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
			return mav;
		} else {
			HashMap<String, Object> paramMap =new HashMap<String, Object>();
			paramMap.put("eseq",eseq);
			paramMap.put("ref_cursor_event", null);
			
			adminService.getEventSelect(paramMap);
			
			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)paramMap.get("ref_cursor_event");
			
			HashMap<String, Object> resultMap = list.get(0);
			
			MEventVO evo = new MEventVO();
			
			evo.setEseq(Integer.parseInt(resultMap.get("ESEQ").toString()));
			evo.setTitle(resultMap.get("TITLE").toString());
			evo.setContent(resultMap.get("CONTENT").toString());
			evo.setSubtitle(resultMap.get("SUBTITLE").toString());
			evo.setWritedate((Timestamp) resultMap.get("WRITEDATE"));
			evo.setStartdate((Timestamp) resultMap.get("STARTDATE"));
			evo.setEnddate((Timestamp) resultMap.get("ENDDATE"));
			
			paramMap.put("ref_cursor_image1",null);

			adminService.getImgesEvent(paramMap);
			
			ArrayList<HashMap<String, Object>> image1List = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor_image1");
			
			HashMap<String, Object> mevimg1 = image1List.get(0);
			evo.setImage1((String) mevimg1.get("IMAGE1"));
			evo.setImage2((String) mevimg1.get("IMAGE2"));
		
			mav.addObject("evo",evo);
			mav.setViewName("admin/customerCenter/adminEventDetail");
		}
		return mav;
	  }
	
	//이벤트 삭제
	@PostMapping("/adminEventDelete")
    public ModelAndView adminEventDelete(HttpServletRequest request
    		, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
    		, @RequestParam("eseq") int eseq) {
		
    	ModelAndView mav= new ModelAndView();
    	HashMap<String , Object>paramMap = new HashMap<String, Object>();
    	
   		if (loginAdmin == null) {
   			mav.setViewName("redirect:/admin");
   		}else {
   			paramMap.put("eseq", eseq);
   			adminService.eventDelete(paramMap);
   			mav.setViewName("redirect:/adminEventList");
   		}
   		return mav;
    }
	
	//이벤트 수정 후 저장
	@PostMapping("/adminEventUpdate")
	public ModelAndView admineventupdate(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @RequestParam("eseq") int eseq
			, @RequestParam("startdate") String startdate
			, @RequestParam("startTime") String startTime
			, @RequestParam("enddate") String enddate
			, @RequestParam("endTime") String endTime
			, @ModelAttribute("evo") @Valid MEventVO evo
			, BindingResult result) {
		
			ModelAndView mav = new ModelAndView();
		
			String startDate = startdate + " " + startTime+":00";
			String endDate = enddate + " " + endTime+":59";

			mav.setViewName("admin/customerCenter/adminEventDetail");
		  
			if(result.getFieldError("title") !=null) {
				mav.addObject("message", result.getFieldError("title").getDefaultMessage());
				  return mav;
			}else if (result.getFieldError("content") !=null) {
				mav.addObject("message", result.getFieldError("content").getDefaultMessage());
					 return mav;
			}else if (result.getFieldError("image1") !=null) {
				mav.addObject("message", result.getFieldError("image1").getDefaultMessage());
					 return mav;
			}else if (result.getFieldError("image2") !=null) {
				mav.addObject("message", result.getFieldError("image2").getDefaultMessage());
					 return mav;
			}else if (result.getFieldError("subtitle") !=null) {
				mav.addObject("message", result.getFieldError("subtitle").getDefaultMessage());
					 return mav;
			}

			if (loginAdmin == null) {
				mav.setViewName("redirect:/admin");
			}else {
				HashMap<String, Object> paramMap = new HashMap<String , Object>();
				paramMap.put("eseq", evo.getEseq());
				paramMap.put("title", evo.getTitle());
				paramMap.put("content", evo.getContent());
				paramMap.put("image1", evo.getImage1());
				paramMap.put("image2", evo.getImage2());
				paramMap.put("subtitle", evo.getSubtitle());
				paramMap.put("startdate", Timestamp.valueOf(startDate));
				paramMap.put("enddate", Timestamp.valueOf(endDate));
				
				adminService.eventUpdate(paramMap);
				
				mav.addObject("eseq", eseq);
				mav.addObject("startdate", startdate);
				mav.addObject("enddate", enddate);
				mav.addObject("startTime", startTime);
				mav.addObject("endTime", endTime);
				mav.addObject(evo);
				
				mav.setViewName("redirect:/adminEventDetail");
			}
			return mav;
	  }
	
	//이벤트 등록 폼
	@PostMapping("/adminEventInsertForm")
	public ModelAndView adminEventinsertform(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin) {
		
		ModelAndView mav = new ModelAndView();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			mav.setViewName("admin/customerCenter/adminEventInsertForm");
		}
		return mav;
	}
	
	//이벤트 등록 후 저장
	@PostMapping("/adminEventInsert")
    public ModelAndView adminEventinsert(HttpServletRequest request
    		, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @RequestParam("startTime") String startTime
			, @RequestParam("endTime") String endTime
			, @RequestParam("startdate") String startdate
			, @RequestParam("enddate") String enddate
			, @ModelAttribute("evo") @Valid MEventVO evo
			, BindingResult result) {
		    	
		ModelAndView mav= new ModelAndView();
		
		mav.setViewName("admin/customerCenter/adminEventInsertForm");
	 
		if(result.getFieldError("title") !=null) {
			mav.addObject("message", result.getFieldError("title").getDefaultMessage());
			return mav;
		}else if (result.getFieldError("content") !=null) {
			mav.addObject("message", result.getFieldError("content").getDefaultMessage());
			return mav;
		}else if (result.getFieldError("image1") !=null) {
			mav.addObject("message", result.getFieldError("image1").getDefaultMessage());
			return mav;
		}else if (result.getFieldError("image2") !=null) {
			mav.addObject("message", result.getFieldError("image2").getDefaultMessage());
			return mav;  
		}else if (result.getFieldError("subtitle") !=null) {
			mav.addObject("message", result.getFieldError("subtitle").getDefaultMessage());
			return mav;
		
		}
		String startDate = startdate + " " + startTime + ":00";
		String endDate = enddate + " " + endTime + ":59";

		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		}else {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("eseq", evo.getEseq());
			paramMap.put("title", evo.getTitle());
			paramMap.put("content", evo.getContent());
			paramMap.put("image1", evo.getImage1());
			paramMap.put("image2", evo.getImage2());
			paramMap.put("subtitle", evo.getSubtitle());
			paramMap.put("startdate", Timestamp.valueOf(startDate));
			paramMap.put("enddate", Timestamp.valueOf(endDate));
		
			adminService.eventInsert(paramMap);
		
			mav.addObject("enddate", enddate);
			mav.addObject("startdate", startdate);
			mav.addObject("endTime", endTime);
			mav.addObject("startTime", startTime);
			mav.setViewName("redirect:/adminEventList");
		}
		return mav;
    }
	
	//메인배너관리 목록 조회
	@GetMapping("/adminBannerList")
	public ModelAndView adminBannerList(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @RequestParam(value = "message", required = false) String message) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			setPageAndKey(request, session, paramMap);
			
			paramMap.put("ref_cursor", null);
			adminService.getBannerList(paramMap);
			
			ArrayList<HashMap<String, Object>> bannerList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			request.setAttribute("bannerList", bannerList);
			mav.addObject("message", message);
			mav.setViewName("admin/banner/adminBannerList");
		}
		return mav;
	}
	
	//메인배너관리 배너저장
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping("/bannerSave")
	public ModelAndView bannerSave(HttpServletRequest request
			, @SessionAttribute(name = "loggedinAdmin", required = false)  MAdminVO loginAdmin
			, @RequestParam(value = "num", required = false) int[] numArr
			, @RequestParam(value = "name", required = false) String[] nameArr
			, @RequestParam(value = "image", required = false) String[] imageArr
			, @RequestParam(value = "url", required = false) String[] urlArr) {
	
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		if (loginAdmin == null) {
			mav.setViewName("redirect:/admin");
		} else {
			adminService.deleteBanner();
			
			for(int i = 0; i< numArr.length; i++) {
				paramMap.put("num", numArr[i]);
				paramMap.put("name", nameArr[i]);
				paramMap.put("image", imageArr[i]);
				paramMap.put("url", urlArr[i]);
				adminService.insertBanner(paramMap);
			}
			mav.addObject("message","배너 설정이 완료되었습니다.");
			mav.setViewName("redirect:/adminBannerList");
		}
		return mav;
	}
	
	@RequestMapping("imageLoad")
	public String imageLoad(HttpServletRequest request,
		@RequestParam(value = "num", required = false) int num) {
		request.setAttribute("num", num);
		return "admin/banner/imageLoad";
	}
	
}
