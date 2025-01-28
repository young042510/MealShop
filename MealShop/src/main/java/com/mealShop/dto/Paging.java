package com.mealShop.dto;

import lombok.Data;

@Data
public class Paging {

    private int page = 1; // 현재 페이지 번호
    private int totalCount; // 게시물 총 갯수
    private int beginPage; // 시작 페이지 번호
    private int endPage; // 끝 페이지 번호
    private int displayRow = 12; // 한 화면에 표시될 게시물 수
    private int displayPage = 5; // 한 화면에 표시될 페이지 수
    private boolean prev; // 이전 버튼 활성화 여부
    private boolean next; // 다음 버튼 활성화 여부
    private int startNum; // 시작 게시물 번호
    private int endNum; // 끝 게시물 번호

    /**
     * 페이징 계산 메서드
     */
    private void calculatePaging() {
        // 총 페이지 수 계산
        int totalPage = (int) Math.ceil((double) totalCount / displayRow);

        // 현재 페이지 그룹의 끝 페이지
        endPage = ((int) Math.ceil((double) page / displayPage)) * displayPage;

        // 현재 페이지 그룹의 시작 페이지
        beginPage = endPage - (displayPage - 1);

        // 끝 페이지가 총 페이지 수를 초과하지 않도록 조정
        if (endPage > totalPage) {
            endPage = totalPage;
        }

        // 이전/다음 버튼 활성화 여부
        prev = beginPage > 1;
        next = endPage < totalPage;

        // 게시물 번호 범위 계산
        startNum = (page - 1) * displayRow + 1;
        endNum = Math.min(page * displayRow, totalCount);

        // 디버깅용 출력 (필요 시 주석 처리 가능)
        System.out.printf("Paging Info: beginPage=%d, endPage=%d, startNum=%d, endNum=%d, prev=%b, next=%b%n",
                beginPage, endPage, startNum, endNum, prev, next);
    }

    /**
     * 총 게시물 수를 설정하고 페이징 계산을 트리거
     * 
     * @param totalCount 총 게시물 수
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calculatePaging();
    }
}
