<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../../include/admin/headerfooter/header.jsp"%>
<%@ include file="../../include/admin/sideMenu.jsp"%>

<article id="mypageArticle2" style="width:950px; max-width:950px;">
<h1 align="left">상품 관리</h1>
<form name="frm" method="post">
	<table>
		<tr>
			<td width="460">
				<select name="searchField" id="searchFieldSelect">
					<option value="">선택</option>
					<option value="name">상품명</option>
				</select>
				<input type="text" name="key" value="${key}">
				<input class="btn" type="button" name="btn_search" value="검색" onclick="go_search('adminProductList');">
				<!-- 2025/1/24 전체보기 버튼 제거 > 페이지 오픈되면 바로 목록보이게 구현 -->
				<input class="btn" type="button" name="btn_write" value="상품등록" onclick="go_wrt_Product();">
			</td>
			<td>
				<select name="change_Useyn" size="1" id="productSelect">
					<option value="y" selected>공개</option>
					<option value="n">비공개</option>
				</select>
				<input type="button" class="btn" style="width:100px;" value="체크항목 변경" onclick="go_product_save()">
			</td>
		</tr>
	</table>

	<!-- 상품 목록 -->
	<table id="tableContainer" style="text-align:center;" width="85%">
		<tr>
			<th width="65px">번호</th>
			<th width="200px">상품명</th>
			<th width="80px">원가</th>
			<th width="80px">판매가</th>
			<th width="95px">등록일</th>
			<th width="60px">공개유무</th>
			<th width="50px">선택</th>
		</tr>
		<c:forEach items="${mproductList}" var="mproductVO">
			<tr>
				<td height="23" align="center">${mproductVO.PSEQ}</td>
				<td style="text-align:left; padding=left:50px;">
					<a href="#" onclick="go_Update('${mproductVO.PSEQ}')">${mproductVO.NAME}</a></td>
				<td><fmt:formatNumber value="${mproductVO.PRICE1}"/>원</td>
				<td><fmt:formatNumber value="${mproductVO.PRICE2}"/>원</td>
				<td><fmt:formatDate value="${mproductVO.INDATE}"/></td>
				<td>
					<c:choose>
						<c:when test='${mproductVO.USEYN == "y" }'>
							<span style="font-weight:bold; color:blue">공개</span>
						</c:when>
						<c:otherwise>
							<span style="font-weight:bold; color:red">비공개</span>
						</c:otherwise>
					</c:choose>
				</td>
					<td>
						<input type="checkbox" name="checkBox_pseq" value="${mproductVO.PSEQ}">
					</td>
			</tr>
		</c:forEach>
	</table>
</form>

<div class="clear"></div>
<jsp:include page="../paging/paging.jsp">
	<jsp:param name="command" value="adminProductList" />	
</jsp:include>

</article>


<%@ include file="../../include/admin/headerfooter/footer.jsp"%>