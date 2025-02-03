<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>admin_header</title>
	<link rel="stylesheet" href="admin/css/admin.css">
	<script src="user/script/jquery-3.6.0.js"></script>
	<script src="admin/script/adminProduct.js"></script>
	
	<!-- Ajax -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	
</head>
<body>
<div id="wrap">
	<header>	
	<div>
		<!-- 로그인된 관리자 이름 표시 -->
	    <div id="admin-info" style="text-align: right; margin-right: 10px; margin-top: 10px;">
	        <c:choose>
	            <c:when test="${not empty sessionScope.loggedinAdmin.name}">
	                <span>안녕하세요, 관리자 <strong>${sessionScope.loggedinAdmin.name}</strong> 님</span>
	            </c:when>
	            <c:otherwise>
	                <span>로그인 정보 없음</span>
	            </c:otherwise>
	        </c:choose>
		<input class="btn" type="button" value="로그아웃" style="float: right;" onClick="location.href='adminLogout'">	
		<input class="btn" type="button" value="홈으로 이동" style="float: right;" onClick="location.href='/'">	
		<div class="clear"></div>
	    </div>		
	</div>
	<!-- 로고 -->
	<div id="logo">
		<div>관리자 라운지</div>
		<a href="adminProductList"><img src="images/Mealogo.png"></a>		
	</div>	
	</header>
	<div class="clear"></div>