<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/admin/headerfooter/header.jsp"%>
<%@ include file="../../include/admin/sideMenu.jsp"%>

<article id="mypageArticle2" style="width:950px; max-width:950px;">
<h1>비회원문의 </h1>
<form name="form4" method="post" action="meal.do">
<input type="hidden" name="nqseq"  value="${nmqnaVO.NQSEQ}">

<table id="qnadetil2" style="text-align:center;">
<tr><th width="120px;" height="80px">회원아이디</th><td>${nmqnaVO.ID}</td>
<th width="100px;" height="80px">날짜</th><td>${nmqnaVO.INDATE} &nbsp;등록</tr>
<tr><th colspan="2" width="100px;" height="40px" >제목</th>
<td width="200px;"   height="20px">${nmqnaVO.SUBJECT}</td><td><td></td><td></tr>
<tr><th colspan="4" width="100px;" height="20px"  >내용</th><td></td><td></td><td></td></tr>
<tr><td colspan="4"  width="100px;" height="80px">${nmqnaVO.CONTENT}</td><td></td><td></td><td></td></tr>
</table>

<c:choose>
<c:when test='${nmqnaVO.REP=="1"}'>
<table id="qnaList2">
<tr><td width="100px;"> 답변 작성 </td><td><td colspan="2"><textarea name="reply" rows="3" cols="50">${nmqnaVO.REPLY }</textarea></td><td></td></tr>
 <tr><td colspan="4" style="text-align:center;" height="50px;"><input type="button" class="btn" value="저장" onClick="go_rep2()">
 <input type="button" class="btn" value="목록" onClick="location.href='adminnmQnaList'">
</td><td></td><td></td><td></td></tr>
</table>
</c:when>
<c:otherwise>
<table id="qnaList2">
<tr><td  height="50px;" width="100px"> 답변 작성 </td><td width="400px" style="word-break:break-all;" ><textarea name="reply" rows="3" cols="50">${nmqnaVO.REPLY }</textarea></td></tr>
<tr><td colspan="4" style="text-align:center;" height="50px;"><input type="button" class="btn" value="목록" onClick="location.href='adminnmQnaList'">
<input type="button" class="btn" value="수정" onClick="go_rep2()">
</td><td></td><td></td></tr>
</table>
</c:otherwise>
</c:choose>

</form>
 <div id="message" style="font-size:1.4em;"> ${message}</div>
</article>
<%@ include file="../../include/admin/headerfooter/footer.jsp"%>