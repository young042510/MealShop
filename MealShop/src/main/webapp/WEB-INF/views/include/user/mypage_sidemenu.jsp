<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 	
<nav id="sideMenu">
	<ul>
		<li><p id="mypage"><a href="mypage">MY PAGE</a></li>
		<br>
		<li><h3><a href="orderList">나의 쇼핑</a></h3></li>
		<li id=menu><a href="orderList">주문/배송</a></li>
		<li id=menu><a href="orderCancelForm">주문 취소</a></li>
		<br>
		<li><h3><a href="qnaForm">나의 활동</a></h3></li>
		<li id=menu><a href="qnaForm?sub=y">나의 Q&amp;A</a></li>
		<li id=menu><a href="askForm?sub=y">나의 상품 문의</a></li>
		<li id=menu><a href="reviewForm?sub=y">나의 상품 후기</a></li>
		<br>
		<li><h3><a href="updateForm">나의 정보</a></h3></li>
		<li id=menu><a href="updateForm">회원정보 수정</a></li>
		<li id=menu><a href="withDrawal" 
		onclick="return confirm('<확인>을 누를시 일정기간 후 사용기록이 모두 삭제됩니다, 정말 회원탈퇴 하시겠습니까?')">회원 탈퇴</a></li>
	</ul>
</nav>