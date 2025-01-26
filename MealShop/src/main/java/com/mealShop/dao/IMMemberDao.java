package com.mealShop.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMMemberDao {
	
	// 회원 정보 가져오기
	void getMember(HashMap<String, Object> paramMap);
	
	// 회원 추가
	void insertMember(HashMap<String, Object> paramMap);
	
	// 회원 로그인
	void selectAddressByDong(HashMap<String, Object> paramMap);

	// 회원 정보 수정
	void updateMember(HashMap<String, Object> paramMap);

	// 회원 탈퇴
	void updateUseyn(HashMap<String, Object> paramMap);

	// 회원이메일로 아이디 찾기
	void getMemberByemail(HashMap<String, Object> paramMap);

	// 회원번호로 아이디 찾기
	void getMemberByphone(HashMap<String, Object> paramMap);

	// 회원 정보 조회 (admin)
	void listMember(HashMap<String, Object> paramMap);

	// 회원 수정 (admin)
	void updateMemberResult(HashMap<String, Object> paramMap);
	
}
