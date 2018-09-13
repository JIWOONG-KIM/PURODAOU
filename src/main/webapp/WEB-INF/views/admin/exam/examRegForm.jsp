<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
<%@ include file="../common/import.jsp"%>
<body>
	<div class="container-fluid">
		<div class="row content">
		<%@ include file="../common/lnb.jsp"%>
			<div class="col-sm-9">
				<h4 class="s_title" style="padding-top:25px; padding-bottom:15px">
					<span class="span-font">Home > 출제관리</span>
				</h4>
				<hr><br>

				<form name="exam_reg_form" id="exam_reg_form" method="post">
					<div class="container">
						<h2 class="span-font">출제하기 페이지</h2>
						<p class="span-font">풀어다우 출제 등록 페이지 입니다.</p>

						<table class="table table-hover">

							<tr>
								<th scope="row" width="15%">카테고리 설정</th>
								<td>
									<select id="exam_category"  name="exam_category" class="form-control" style="float:left; width:25%;">
									<option value="">카테고리를 선택하세요.</option>
										<c:forEach var="item" items="${categoryList}">
											<option value="${item.category_no}">${item.category_name}</option>
										</c:forEach>
									</select>
								</td>						
							</tr>
							
			
							<tr>
								<th scope="row">시험기간 설정</th>
								<td>
									<input type="text" name="exam_start_date" id="exam_start_date" class="form-control"  placeholder="시작일자를 지정하세요." style="float:left; width:20%; margin-right:15px;"> 
									<input type="text" name="exam_end_date" id="exam_end_date" class="form-control"  placeholder="종료일자를 지정하세요."style="float:left; width:20%; margin-right:15px;"> 		
								</td>
							
							</tr>

							<tr>
								<th scope="row">합격 커트라인 점수</th>
								<td>
									<input type="text" name="exam_pass_score" id="exam_pass_score" class="form-control" name="score" style="width:5%;float:left"/> &nbsp;&nbsp;점 
								</td>
							</tr>
							
							
							<tr style="height:70px">
								<th scope="row">부서</th>
								<td>
									<input type="checkbox" value="all" name="exam_category"> 전체&nbsp;&nbsp;
									<c:forEach var="item" items="${deptList}">
										<input type="checkbox" name="emp_dept" id="emp_dept" value="${item.quiz_cfg_code}"> ${item.quiz_cfg_code_name} &nbsp;&nbsp;
									</c:forEach>
								</td>
							</tr>
							
							
							<tr style="height:70px">
								<th scope="row">직급</th>
								<td>
									<input type="checkbox" value="all"> 전체&nbsp;&nbsp;
									<c:forEach var="item" items="${gradeList}">
										<input type="checkbox" name="emp_grade" id="emp_grade" value="${item.quiz_cfg_code}"> ${item.quiz_cfg_code_name} &nbsp;&nbsp;
									</c:forEach>
								</td>
							</tr>

							
							<tr>
								<th scope="row">유형별 문제 수<br>(총 20문항)</th>
								<td>
								<div style="float:left"><p style="margin-left:20%">O/X</p> <input type="text" name="exam_ox_num" id="exam_ox_num" class="form-control" name="score" style="width:50%" /></div>
								<div style="float:left"><p style="margin-left:15%">객관식</p> <input type="text" name="exam_obj_num" id="exam_obj_num" class="form-control" name="score" style="width:50%" /></div>
								<div style="float:left"><p style="margin-left:15%">주관식</p> <input type="text" name="exam_short_num" id="exam_short_num" class="form-control" name="score" style="width:50%" /></div>
								</td>
							</tr>
						</table>

						<table class="table table-hover">
							<tr>
								<td>
									<input type="button" value="취소" onclick="goList();" class="btn btn-default" style="float: right" /> 
									<input type="button" value="출제" onclick="goReg(); return false;" class="btn btn-default" style="float: right; margin-right: 5px" />
								</td>
							</tr>
						</table>
					</div>
				</form>
			 </div>
			</div>
		</div>

	<%@ include file="../common/footer.jsp"%>

	<script type="text/javascript">
		function goList(){
			location.href="examList.daou";
		}
		
		function goReg(){
			var queryString = $("form[name=exam_reg_form]").serialize();

			$.ajax({
				type : "POST",
				url : "examReg.daou",
				data : queryString,
				async : false,
				success : function(data) {
					if (data == "success") {
						alert("출제되었습니다.");
						location.href = "examList.daou";
					} else if (data == "error") {
						alert("등록에 실패하였습니다.");
						return;
					}
				},
				error : function(data) {
					alert("등록에 실패하였습니다.");
				}
			});
		}
	</script>
	
	
</body>
</html>