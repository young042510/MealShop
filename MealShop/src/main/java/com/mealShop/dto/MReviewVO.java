package com.mealShop.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MReviewVO {

	private Integer rseq;
	private Integer pseq;
	
	@NotNull(message="내용을 입력하세요!")
	@NotEmpty(message="내용을 입력하세요!")
	@NotBlank(message="내용입력해주세요!")
	private String content;
	
	private String id;
	private Timestamp indate;
	private String pname;
	
}
