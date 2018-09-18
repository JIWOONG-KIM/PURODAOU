package com.daou.pd.admin.exam;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.daou.pd.admin.employee.ConfigVO;
import com.daou.pd.admin.employee.EmpService;
import com.daou.pd.admin.employee.EmpVO;
import com.daou.pd.admin.item.ItemService;
import com.daou.pd.admin.item.ItemVO;
import com.daou.pd.user.exam.ExamDetailVO;
import com.daou.pd.user.exam.ExamUserVO;
import com.daou.pd.user.exam.ExamVO;

@Controller
public class AdminExamController {
	@Autowired
	@Resource(name = "adminExamService")
	private AdminExamService adminExamService;
	
	@Autowired
	@Resource(name = "EmpService")
	private EmpService empService;
	
	@Autowired
	@Resource(name = "itemService")
	private ItemService itemService;

	@RequestMapping(value = "admin/examList.daou")
	public ModelAndView itemList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("admin/exam/examList");
		
		List<ItemVO> categoryList = itemService.categoryList();
		List<ExamVO> examList = adminExamService.examList();
		
		mav.addObject("categoryList", categoryList);
		mav.addObject("examList", examList);
		
		return mav;
	}

	
	@RequestMapping(value = "admin/examRegForm.daou")
	public ModelAndView itemRegForm(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("admin/exam/examRegForm");
		List<EmpVO> deptList = empService.deptList();
		List<EmpVO> gradeList = empService.gradeList();
		
		List<ItemVO> categoryList = itemService.categoryList();

		mav.addObject("categoryList", categoryList);
		
		mav.addObject("deptList", deptList);
		mav.addObject("gradeList", gradeList);
		
		return mav;
	}
	
	
	@RequestMapping(value = "admin/examReg.daou")
	public ModelAndView examReg(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "emp_dept") List<String> emp_dept, 
			@RequestParam(value = "emp_grade") List<String> emp_grade, ExamVO evo) {
		
		ModelAndView mav = new ModelAndView("admin/exam/result");
		
		String msg = "";
		int exam_reg_result = adminExamService.examReg(evo);

	
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("emp_dept", emp_dept);
		data.put("emp_grade",emp_grade);

		List<String> empIdList = adminExamService.selectExamTarget(data);
		
		for(int i=0; i< empIdList.size(); i++) {
			adminExamService.userExamReg(empIdList.get(i));
		}
		
		if(exam_reg_result == 1) {
			msg = "success";
		} else {
			msg = "error";
		}
		
		mav.addObject("resultCode", msg);
		return mav;
	}
	
	@RequestMapping(value = "admin/examListExcel.daou")
	public ModelAndView examListExcel(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Dddd");
		ModelAndView mav = new ModelAndView("admin/exam/examListExcel");
		
		List<ItemVO> categoryList = itemService.categoryList();
		List<ExamVO> examList = adminExamService.examList();
		
		mav.addObject("categoryList", categoryList);
		mav.addObject("examList", examList);
		
		return mav;
	}
	
	@RequestMapping(value = "admin/recordList.daou")
	public ModelAndView recordList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("admin/exam/recordList");
		
		List<HashMap<String, Object>> recordList = adminExamService.recordList();
		List<ConfigVO> statusCodeList = adminExamService.statusCodeList();
		
		mav.addObject("recordList", recordList);
		mav.addObject("statusCodeList", statusCodeList);
		
		return mav;
	}
	
	
	@RequestMapping(value = "admin/recordGradeForm.daou")
	public ModelAndView recordGradeForm(HttpServletRequest request, HttpServletResponse response, ExamUserVO euvo) {
		ModelAndView mav = new ModelAndView("admin/exam/recordGradeForm");
		
		List<HashMap<String,Object>> shortExamList = adminExamService.getShortExamList(euvo);
		mav.addObject("shortExamList", shortExamList);
		
		return mav;
	}
	
	@RequestMapping(value = "admin/recordGrade.daou")
	public ModelAndView recordGrade(HttpServletRequest request, HttpServletResponse response, ExamDetailVO euvo) {
		
		ModelAndView mav = new ModelAndView("admin/exam/result");
		
		int updtExamDetail = 0;
		int updtExamScore = 0;
		int selectPassScore = 0;
		int exam_score = 0;
		int finalExamUpdate = 0;
		int insertFailUser = 0;
		
		String msg="";
	
		try {
			
			 String[] arr_item_index = request.getParameterValues("item_index");
			    String[] arr_correct_yn = request.getParameterValues("correct_yn");
			    String[] exam_detail_comment = request.getParameterValues("exam_detail_comment");
			    String[] exam_no = request.getParameterValues("exam_no");
			    String exam_degree = request.getParameter("exam_degree");
			    String exam_take_id = request.getParameter("exam_take_id");
			    String exam_retake_degree = request.getParameter("exam_retake_degree");
			    
			    
			    // 주관식 정답여부를 가져와서 exam_detail테이블에서 correct_yn을 update!
			    for(int i =0; i < arr_item_index.length; i++) {
				     euvo.setExam_detail_no(Integer.parseInt(arr_item_index[i]));
				  	 euvo.setCorrect_yn(arr_correct_yn[i]);
				  	 euvo.setExam_no(Integer.parseInt(exam_no[i]));
				     euvo.setExam_detail_comment(exam_detail_comment[i]);

				     updtExamDetail += adminExamService.updtExamDetail(euvo);
			     }
			    

			    // quiz_exam_detail테이블에서 exam_no로 correct_yn 개수를 전부 빼옴!
			    // quiz_user_exam에 correct_yn * 5 한 값을 가져옴
			    exam_score = adminExamService.selectTotalCorrectYn(euvo) * 5;
			    
			    
			    // 그 값을 quiz_user_exam의 exam_score에 넣어줌 -> euvo에 exam_score값이 없음
			    ExamUserVO evo = new ExamUserVO();
			    
			    evo.setExam_take_id(exam_take_id);
			    evo.setExam_no(euvo.getExam_no());
			    evo.setExam_score(exam_score);
			    evo.setExam_degree(Integer.parseInt(exam_degree));
			    updtExamScore += adminExamService.updtExamScore(evo);
			    
			    
			 
			    // exam_score가 quiz_user_exam테이블의 exam_pass_score보다 적으면 상택값을 status04 or status05로 하고, exam_final_yn의 값을 N으로 한다.
			    selectPassScore = adminExamService.selectPassScore(evo);
			    
			    if(exam_score >= selectPassScore) {
			    	evo.setExam_status("status04"); // 합격
			    	evo.setExam_final_yn("Y");
			    	finalExamUpdate = adminExamService.finalExamUpdate(evo);
			    }else { 
			    	evo.setExam_status("status05"); // 불합격
			    	evo.setExam_final_yn("N");
			    	finalExamUpdate = adminExamService.finalExamUpdate(evo);
			    }

			    
			    // exam_final_yn이 N이면, retake_degree_no의 값을 +1증가 시켜서 다시 quiz_user_exam에 저장한다.(insert)
			    if(evo.getExam_final_yn().equals("N")){
			    	evo.setExam_retake_degree(Integer.parseInt(exam_retake_degree)+1); // 1회차 더 봐야하기 떄문에 retake_degree +1
			    	
			    	insertFailUser = adminExamService.insertFailUser(evo);
			    }
			
			    msg="success";
			    
		}catch(Exception e) {
			mav.addObject("resultCode", "error");
		}
	   
		mav.addObject("resultCode", msg);
		
		return mav; 
	}
	
	@RequestMapping(value = "admin/recordListExcel.daou")
	public ModelAndView recordListExcel(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("admin/exam/recordListExcel");
		
		List<HashMap<String, Object>> recordList = adminExamService.recordList();
		List<ConfigVO> statusCodeList = adminExamService.statusCodeList();
		
		mav.addObject("recordList", recordList);
		mav.addObject("statusCodeList", statusCodeList);
		
		return mav;
	}
	
	
}
