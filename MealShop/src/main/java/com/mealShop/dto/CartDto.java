package com.mealShop.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CartDto {
    private int cseq;          // 장바구니 번호
    private String id;         // 사용자 ID
    private int pseq;          // 상품 번호
    private int quantity;      // 수량
    private String result;     // 처리 상태
    private Date indate;       // 날짜
}
