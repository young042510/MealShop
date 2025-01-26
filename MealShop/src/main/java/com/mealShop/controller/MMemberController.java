package com.mealShop.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mealShop.dto.MMemberVO;
import com.mealShop.service.MCartService;
import com.mealShop.service.MMemberService;
import com.mealShop.service.MOrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MMemberController {

	private final MMemberService ms;
	
	private final MOrderService os;
	
	private final MCartService cs;
	
	
	// 로그인 폼
	@GetMapping(value = "/userLogin")
	public String loginForm(@ModelAttribute("message") String message) {
		return "member/login";
	}
	
	@PostMapping(value="/login")
	public String login( @ModelAttribute("dto") @Valid MMemberVO membervo , BindingResult result, 
			HttpServletRequest request, Model model) {
		
		int cnt = 0;	// 카트 개수 초기화
		// 아이디, 비번 빈칸확인
		if( result.getFieldError("id") != null ) {
			model.addAttribute("message", result.getFieldError("id").getDefaultMessage() );
			return "member/login";
		}else if( result.getFieldError("pwd")!=null) {
			model.addAttribute("message", result.getFieldError("pwd").getDefaultMessage() );
			return "member/login";
		}
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", membervo.getId());
		paramMap.put("ref_cursor", null);
		
		ms.getMember( paramMap );  
		
		// 리스트 조회
		ArrayList< HashMap<String, Object> >list 
			= (ArrayList< HashMap<String, Object> >)paramMap.get("ref_cursor");
		if( list.size() == 0 ) {
			model.addAttribute("message", "밀조) 아이디가 없습니다");
			return "member/login";
		}
		
		
		
		HashMap<String, Object> mvo = list.get(0);
		
		if( mvo.get("PWD") == null ) {
			model.addAttribute("message", "밀조) 비밀번호 오류. 밀조왕에게 문의하세요");
			return "member/login";
		}else if( !mvo.get("PWD").equals(membervo.getPwd())) {
			model.addAttribute("message", "밀조) 비밀번호가 맞지않습니다");
			return "member/login";
		}else if( mvo.get("USEYN").equals("n")) {
			//model.addAttribute("message", "밀조) 탈퇴하거나 휴먼중인 계정입니다. 하단 공지에서 비회원 문의를 통해 밀조왕에게 문의하세요");
			model.addAttribute("useyn2", "n");
			return "member/login";
		}else if( mvo.get("PWD").equals(membervo.getPwd())) {
			HttpSession session = request.getSession();
			
			String redirectUrl = (String)session.getAttribute("redirectUrl");
			
			session.setAttribute("loginUser", mvo);
			
			// 카트 개수 세션에 담기
			paramMap.put("cnt", 0);	// 카드 개수 담아올 변수
			cs.getCartCnt(paramMap);
			System.out.println(paramMap.get("cnt"));
			cnt = Integer.parseInt(paramMap.get("cnt").toString());
			session.setAttribute("cartCnt",cnt);
			if(redirectUrl!=null) return "redirect:/"+(String)redirectUrl;
			else return "redirect:/";
			
			
		}else {
			model.addAttribute("message", "밀조) 무슨이유인지 모르겠지만 로그인 안돼요");
			return "member/login";
		}
		
	}
	
	
	
	// 로그아웃처리
	@PostMapping(value="/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("loginUser");
		// 카트 개수 세션 삭제
		session.removeAttribute("cartCnt");
		session.removeAttribute("redirectUrl");
		return "redirect:/";
	}
	
	
	@GetMapping(value="/contract")
	public String contract() {
		return "member/contract";
	}
	
	
	@GetMapping("/joinForm")
	public String join_form( ) {
		return "member/join";
	}
	
	// 아이디 중복찾기 폼
	@GetMapping("/idcheck")
	public String idcheckForm() {
	    return "member/idcheck"; // JSP 화면 반환
	}
	
	// 회원가입 아이디 중복 체크
	@PostMapping("/idcheck")
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
	
	@GetMapping("/findZipNum")
	public String findZipNumForm() {
	    return "member/findZipNum"; // JSP 화면 반환
	}
	
	// 주소찾기
	@PostMapping(value="/findZipNum")
	public String find_zip( HttpServletRequest request , Model model) {
		String dong=request.getParameter("dong");
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		if(dong != null && dong.trim().equals("")==false){			
			paramMap.put( "ref_cursor", null );
			paramMap.put("dong", dong);
			
			ms.selectAddressByDong(paramMap);
			
			ArrayList< HashMap<String,Object> > list 
				= (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
			
			model.addAttribute("addressList" , list);
		}
		return "member/findZipNum";
	}
	
	// 회원가입 완료처리
	@RequestMapping(value="/join")
	public ModelAndView memberJoin( 
			@ModelAttribute("dto") @Valid MMemberVO membervo,
			BindingResult result, 
			@RequestParam("reid") String reid, 
			@RequestParam("pwdCheck") String pwchk, 
			@RequestParam("addr1") String address1, 
			@RequestParam("addr2") String address2, 
			Model model ) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("member/join");  
		
		
		if( reid != null && reid.equals("") ) mav.addObject("reid", reid);
		if( result.getFieldError("id")!=null) 
			mav.addObject("message", result.getFieldError("id").getDefaultMessage() );
		else if( result.getFieldError("pwd") != null ) 
			mav.addObject("message", result.getFieldError("pwd").getDefaultMessage() );
		else if( result.getFieldError("name") != null ) 
			mav.addObject("message", result.getFieldError("name").getDefaultMessage() );
		else if( result.getFieldError("email") != null ) 
			mav.addObject("message", result.getFieldError("email").getDefaultMessage() );
		else if( result.getFieldError("phone") != null ) 
			mav.addObject("message", result.getFieldError("phone").getDefaultMessage() );
		else if( !membervo.getId().equals(reid)) 
			mav.addObject("message","아이디 중복체크가 되지 않았습니다");
		else if( !membervo.getPwd().equals(pwchk)) 
			mav.addObject("message","비밀번호 확인이 일치하시 않습니다.");
		else {  
		
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", membervo.getId());
			paramMap.put("pwd", membervo.getPwd());
			paramMap.put("name", membervo.getName());
			paramMap.put("email", membervo.getEmail());
			paramMap.put("phone", membervo.getPhone());
			paramMap.put("zip_num", membervo.getZip_num());
			paramMap.put("address", address1 + " " + address2);
			
			ms.insertMember( paramMap );
			
			mav.addObject("message", "회원가입이 완료되었습니다. 로그인 하세요");
			mav.setViewName("member/login"); 
		}
		return mav;
	}
	
	
	@GetMapping(value = "/updateForm")
	public ModelAndView updateForm( 
			@ModelAttribute("dto") @Valid MMemberVO membervo,
			BindingResult result, 
			HttpServletRequest request,
			Model model ) {
		ModelAndView mav = new ModelAndView(); 
		
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser 
		= (HashMap<String, Object>)session.getAttribute("loginUser");
		
		String address = (String)loginUser.get("ADDRESS");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", (String)loginUser.get("ID"));
		paramMap.put("name", (String)loginUser.get("NAME"));
		paramMap.put("email", (String)loginUser.get("EMAIL"));
		paramMap.put("phone", (String)loginUser.get("PHONE"));
		paramMap.put("zip_num", (String)loginUser.get("ZIP_NUM"));
		paramMap.put("address", address);
		
		if( address != null) {
			int k1 = address.indexOf(" ");
			int k2 = address.indexOf(" ", k1+1); 
			int k3 = address.indexOf(" ", k2+1); 
			
			
			String addr1 = address.substring(0, k3);
			
			String addr2 = address.substring(k3+1);
			
			mav.addObject("addr1", addr1);
			mav.addObject("addr2", addr2);
			
		}
		mav.setViewName("member/update");
		return mav;
	}

	
	
	
	@PostMapping(value = "/memberUpdate")
	public String memberUpdate( @ModelAttribute("dto") @Valid MMemberVO membervo,
			BindingResult result,
			@RequestParam(value="pwdCheck", required=false) String pwdCheck,
			@RequestParam("addr1") String address1, 
			@RequestParam("addr2") String address2,
			HttpServletRequest request,
			Model model ) {
		
		if( result.getFieldError("pwd") != null ) {
			model.addAttribute("message", result.getFieldError("pwd").getDefaultMessage() );
			return "member/update";
		} else if( result.getFieldError("name") != null ) {
			model.addAttribute("message", result.getFieldError("name").getDefaultMessage() );
			return "member/update";
		} else if( result.getFieldError("email") != null ) {
			model.addAttribute("message", result.getFieldError("email").getDefaultMessage() );
			return "member/update";
		} else if( result.getFieldError("phone") != null ) {
			model.addAttribute("message", result.getFieldError("phone").getDefaultMessage() );
			return "member/update";
		} else if( pwdCheck == null || (  pwdCheck != null && !pwdCheck.equals(membervo.getPwd() ) ) ) {
			model.addAttribute("message", "비밀번호 확인 일치하지 않습니다");
			return "member/update";
		}
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", membervo.getId() );
		paramMap.put("PWD", membervo.getPwd() );
		paramMap.put("NAME", membervo.getName() );
		paramMap.put("EMAIL", membervo.getEmail() );
		paramMap.put("PHONE", membervo.getPhone() );
		paramMap.put("ZIP_NUM", membervo.getZip_num() );
		paramMap.put("ADDRESS", address1 + " " + address2 );

		ms.updateMember( paramMap );
		
		HttpSession session = request.getSession();
		session.setAttribute("loginUser", paramMap);

		return "redirect:/";
	}
	
	@RequestMapping(value = "/withDrawal")
	public ModelAndView withDrawal( 
			@ModelAttribute("dto") @Valid MMemberVO membervo,
			BindingResult result, 
			HttpServletRequest request,
			Model model ) {
		ModelAndView mav = new ModelAndView(); 
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		mav.setViewName("member/completeWithdrawal");
		
		HttpSession session = request.getSession();
		HashMap<String, Object> loginUser 
		= (HashMap<String, Object>)session.getAttribute("loginUser");
		
		if( loginUser == null ) {
			mav.setViewName("member/login");
		} else {
			//System.out.println((String)loginUser.get("ID"));
			paramMap.put("id", (String)loginUser.get("ID"));
			paramMap.put("ref_cursor", null);
			
			//mdao.updateUseyn( mvo.getId() );
			ms.updateUseyn( paramMap );
			
			//cdao.deleteCart( mvo.getId() );
			cs.deleteCart2( paramMap );
			
			//ArrayList<Integer> oseqList	= odao.selectOseqOrderAll( mvo.getId() );
			os.selectOseqOrderAll( paramMap );
			
			ArrayList< HashMap<String, Object> >oseqList 
			= (ArrayList< HashMap<String, Object> >)paramMap.get("ref_cursor");
			
			for(HashMap<String, Object> oseq : oseqList) {
				paramMap.put("oseq", oseqList);
				//odao.deleteOrders(oseq);
				os.deleteOrders( paramMap );
				//odao.deleteOrder_detail(oseq);
				os.deleteOrder_detail( paramMap );
				
				System.out.println(paramMap);
				System.out.println(oseqList);
				System.out.println(oseq);
			}
			session.removeAttribute("loginUser");
			
			// 카트 개수 세션 삭제
			session.removeAttribute("cartCnt");
		}
		return mav;
	}
	
	@GetMapping(value = "/findIdForm")
	public String findIdForm() {
	return "member/findIdForm";
	}
	
	
	@PostMapping(value = "/findIdStep1")
	public ModelAndView findIdStep1( 
			@ModelAttribute("dto") @Valid MMemberVO membervo,
			BindingResult result, 
			@RequestParam("selected2") String selected2,
			HttpServletRequest request,
			Model model ) {
		ModelAndView mav = new ModelAndView(); 
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		mav.setViewName("member/findIdForm");
		
		if(membervo.getEmail() != null && !"".equals(membervo.getEmail())) {
			//mmemberVO mvo = mdao.getMemberByemail(name, email);
			paramMap.put("email", membervo.getEmail());
			paramMap.put("name", membervo.getName());
			paramMap.put("ref_cursor", null);
			
			ms.getMemberByemail(paramMap);
			
			ArrayList< HashMap<String, Object> >list 
			= (ArrayList< HashMap<String, Object>>)paramMap.get("ref_cursor");
		
			if( list.size() == 0 ) {
				model.addAttribute("msg", "해당회원이 없습니다");
				//mav.setViewName("member/findIdForm");
			} else {
				HashMap<String , Object > mvo = list.get(0);
				String id = (String)mvo.get("ID");
				String pwd = (String)mvo.get("PWD");
				
				model.addAttribute("id", id);
				model.addAttribute("pwd", pwd);
				model.addAttribute("email", membervo.getEmail());
				mav.setViewName("member/findIdconfirmNumber");		
				
				//System.out.println(id);
				//System.out.println(pwd);
				
			}	
		}else if(membervo.getPhone() != null && !"".equals(membervo.getPhone())){
			//mmemberVO mvo = mdao.getMemberByphone(name, phone);
			paramMap.put("phone", membervo.getPhone());
			paramMap.put("name", membervo.getName());
			paramMap.put("ref_cursor", null);
			
			ms.getMemberByphone(paramMap);
			
			ArrayList< HashMap<String, Object> > list 
			= (ArrayList< HashMap<String, Object>>)paramMap.get("ref_cursor");
			
			if( list.size() == 0 ) {
				model.addAttribute("msg", "해당회원이 없습니다");
				//mav.setViewName("member/findIdForm");
			} else {
				HashMap<String , Object > mvo = list.get(0);
				String id = (String)mvo.get("ID");
				String pwd = (String)mvo.get("PWD");
				
				model.addAttribute("id", id);
				model.addAttribute("pwd", pwd);
				model.addAttribute("phone", membervo.getPhone());
				mav.setViewName("member/findIdconfirmNumber");
			}	
		}
		model.addAttribute("name", membervo.getName());
		model.addAttribute("selected2", selected2);
		return mav;
	}
	
	
	@PostMapping(value = "/findIdStep2")
	public ModelAndView findIdStep2( 
			@ModelAttribute("dto") @Valid MMemberVO membervo,
			BindingResult result, 
			@RequestParam("confirmNum") String confirmNum,
			@RequestParam("selected2") String selected2,
			HttpServletRequest request,
			Model model ) {
		
		
		ModelAndView mav = new ModelAndView(); 
		model.addAttribute("selected2", selected2);
		model.addAttribute("name", membervo.getName());
		model.addAttribute("pwd", membervo.getPwd());
		model.addAttribute("email", membervo.getEmail());
		model.addAttribute("phone", membervo.getPhone());
		model.addAttribute("id", membervo.getId());
		
		if(!confirmNum.equals("0000")) {
			model.addAttribute("msg", "인증번호가 맞지  않습니다");	
			mav.setViewName("member/findIdconfirmNumber");	
		}else {
			mav.setViewName("member/viewId");
		}
		
		return mav;
	}

}