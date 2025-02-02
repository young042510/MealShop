package com.mealShop.dto;


import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQnaVO {

private Integer qseq;
	
	@NotNull(message="제목을 입력하세요")
	@NotEmpty(message="제목을 입력하세요")
	@NotBlank(message="제목을 입력하세요")
	private String subject;

	@NotBlank(message="제목을 입력하세요")
	private String content;
	
	@NotBlank(message="제목을 입력하세요")
	private String reply;
	
	@NotNull(message="아이디를 입력하세요")
	@NotEmpty(message="아이디를 입력하세요")
	private String id;
	
	@NotNull(message="비밀번호를 입력하세요(비회원문의 확인시 필요하니 기억해두시기 바랍니다)")
	@NotEmpty(message="비밀번호를 입력하세요(비회원문의 확인시 필요하니 기억해두시기 바랍니다)")
	private String pwd;
	
	private String rep;
	private Timestamp indate;
	
	
}
