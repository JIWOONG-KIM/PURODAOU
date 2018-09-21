<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
	<%@ include file="../common/import.jsp"%>
<body>


	<div class="container-fluid">
		<div class="row content">
		<%@ include file="../common/lnb.jsp"%>

			<div class="col-sm-9">
				<h4 class="s_title" style="padding-top: 25px; padding-bottom: 15px">
					<span class="span-font">Home > 기록관리</span>
				</h4>
				<hr>
				<br>

				<form name="recordGradeForm" id="recordGradeForm" method="post">
					<div class="container">
						<h2 class="span-font">주관식 채점 페이지</h2>
						<p class="span-font">풀어다우 주관식 채점 페이지 입니다.</p>
						
						<table class="table table-hover">
							<c:if test="${!empty shortExamList}">
								<c:forEach items="${shortExamList}" var="shortExamList" varStatus="status">
									<input type="hidden" name="exam_no" id="exam_no" value="${shortExamList.exam_no}">
								    <input type="hidden" name="item_index" value="${shortExamList.exam_detail_no}">  
									<input type="hidden" name="exam_degree" value="${shortExamList.exam_degree}">
									<input type="hidden" name="exam_take_id" value="${shortExamList.exam_take_id}">
									<input type="hidden" name="exam_retake_degree" value="${shortExamList.exam_retake_degree}">
									
									<tr>
										<td width="7%">문제유형</td>
										<td>
											<span style="color: red">주관식</span>
										</td>
									</tr>
								
									<tr>
										<td width="7%">문제</td>
										<td>
											<textarea id="item_title" name="item_title" class="form-control" rows="10"  readonly>${shortExamList.item_title}</textarea>
										</td>
									</tr>
		
									<tr>
										<td width="7%">보충자료<br>(50kb 이하)</td>
										<td>
											<c:if test="${empty shortExamList.item_contents}">
												해당 문제는 보충자료가 없습니다.
											</c:if>
											<c:if test="${!empty shortExamList.item_contents}">
												${shortExamList.item_contents}
											</c:if>
										</td>
									</tr>
										
									<tr>
										<td>사용자의 <br> 정답</td>
										<td>
											<textarea id="exam_detail_answer" name="exam_detail_answer" class="form-control" rows="10" readonly>${shortExamList.exam_detail_answer}</textarea>
							       		</td>
									</tr>
									
									<tr>
										<td>실제 정답</td>
										<td>
											<textarea id="exam_detail_correct" name="exam_detail_correct" class="form-control" rows="10" readonly>${shortExamList.exam_detail_correct}</textarea>
							       		</td>
									</tr>
									
									<tr>
										<td>코멘트</td>
										<td>
											<textarea id="exam_detail_comment" name="exam_detail_comment" class="form-control" rows="10"></textarea>
							       		</td>
									</tr>
									
									<tr>
										<td>정답 유무</td>
										<td>
											<input type="radio" name="correct_yn_${shortExamList.exam_detail_no}" class="red_correct_yn" value="Y" id="correct_yn" index="${shortExamList.exam_detail_no}"> 정답
											<input type="radio" name="correct_yn_${shortExamList.exam_detail_no}" class="red_correct_yn" value="N" id="correct_yn" index="${shortExamList.exam_detail_no}"> 오답
											<input type="hidden" name="correct_yn" id="correct_yn" index="${shortExamList.exam_detail_no}"/>
							       		</td>
									</tr>
									
									<tr height="50">
										<td colspan="2">
									</tr>
								</c:forEach>
							</c:if>					
							<c:if test="${empty shortExamList}">
								<tr>
									<td colspan="2"> 주관식이 없습니다. </td>
								</tr>
							</c:if>
						</table>
						
						
						<table class="table table-hover">
							<tr>
								<td>
										<input type="button" value="취소" class="btn btn-default" style="float: right" /> 
									<c:if test="${!empty shortExamList}">
										<input type="button" value="확인" class="btn btn-default" onclick="goUpdt();" style="float: right; margin-right: 5px" />
									</c:if>
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
	
		$(".red_correct_yn").click(function(){
			 var this_index = $(this).attr("index");
			 var this_value = $(this).val();

			 $("input[name='correct_yn'][index='"+this_index+"']").val(this_value);
			 
		}); 
		
			
		function goUpdt(){
			
			//var item_list='';
			
			if($(':radio[id="correct_yn"]:checked').length != ($(':radio[id="correct_yn"]').length/2)){
				alert('정답여부를 선택해주세요.');
				return;
			}
			/*
			
			for(var i =0; i<$("input[name='correct_yn']").length; i++ ){
				alert($("input[name='correct_yn']")[i].attr("index")); //리스트에 넣기
			}
			
			$("input[name='correct_yn']").each(function(){
				item_list += $(this).attr("index");
			});
			
			alert(item_list);
			*/
			
			
			var queryString = $("form[name=recordGradeForm]").serialize();

			$.ajax({
				type : "POST",
				url : "recordGrade.daou",
				data : queryString,
				async : false,
				success : function(data) {
					if (data == "success") {
						alert("채점되었습니다.");
						location.href = "recordList.daou";
					} else if (data == "error") {
						alert("채점에 실패하였습니다.");
						return;
					}
				},
				error : function(data) {
					alert("채점에 실패하였습니다.");
					return;
				}
			});
		}
	</script>
</body>
</html>