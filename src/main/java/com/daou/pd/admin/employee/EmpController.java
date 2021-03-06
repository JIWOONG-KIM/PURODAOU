package com.daou.pd.admin.employee;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmpController {
	@Inject
	@Autowired
	@Resource(name = "EmpService")
	private EmpService empService;
	
	

	final int checkKey = 1;
	
	
	@RequestMapping(value = "admin/login.daou",method=RequestMethod.GET)
	public ModelAndView goLoginPage() {
		ModelAndView mav = new ModelAndView("admin/employee/login");
		return mav;
	}

	
	@RequestMapping(value = "admin/goLogin.daou",method=RequestMethod.POST)
	public ModelAndView goLogin(HttpSession session, HttpServletRequest request, HttpServletResponse response, EmpVO evo) {
	

		int fullCheckResult = empService.selectAdmin(evo);
		int IdCheckResult = empService.employeeIdCheck(evo);

		ModelAndView mav = new ModelAndView("admin/employee/result");
		
		System.out.println("checkKey"+checkKey);
		if (IdCheckResult != checkKey) {
			mav.addObject("resultCode", "IDfail");
		} else if (fullCheckResult == checkKey) {
			session.setAttribute("admin_emp_id", evo.getEmp_id());
			session.setAttribute("admin_emp_name", evo.getEmp_name());
			mav.addObject("resultCode", "success");
		} else {
			mav.addObject("resultCode", "PWfail");
		}
		return mav;
	}

	
	
	
	@RequestMapping(value = "admin/logout.daou")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/employee/login");
		return mav;
	}

	
	
	@RequestMapping(value = "admin/employeeList.daou")
	public ModelAndView goUser(HttpServletRequest request, HttpServletResponse response, EmpVO evo) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/employee/userList");
		
		List<HashMap<String, Object>> memberList = empService.memberList();
		mav.addObject("memberList", memberList);
		return mav;
	}

	
	
	@RequestMapping(value = "admin/employeeRegForm.daou")
	public ModelAndView goReg(HttpServletRequest request, HttpServletResponse response, EmpVO evo) {
		ModelAndView mav = new ModelAndView();
		List<EmpVO> deptList = empService.deptList();
		List<EmpVO> gradeList = empService.gradeList();
		mav.setViewName("admin/employee/userRegForm");
		mav.addObject("deptList", deptList);
		mav.addObject("gradeList", gradeList);
		return mav;
	}

	
	
	@RequestMapping(value = "admin/employeeReg.daou")
	public ModelAndView goInsert(EmpVO evo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("admin/employee/result");
		
		final int userType = 2;
		StringBuffer sbuf = new StringBuffer();
		String txt = "daou" + evo.getEmp_id();
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		mDigest.update(txt.getBytes());
		     
		    byte[] msgStr = mDigest.digest() ;
		     
		    for(int i=0; i < msgStr.length; i++){
		        byte tmpStrByte = msgStr[i];
		        String tmpEncTxt = Integer.toString((tmpStrByte & 0xff) + 0x100, 16).substring(1);
		         
		    sbuf.append(tmpEncTxt) ;
		    }
		evo.setEmp_pw(sbuf.toString());
		evo.setEmp_type(userType);
		evo.setUse_yn("Y");
		empService.insertMember(evo);
		mav.addObject("resultCode", "success");
		return mav;
	}

	
	
	@RequestMapping(value = "admin/employeeDlt.daou")
	public ModelAndView goDel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "list[]") String[] delList) throws Exception {

		ModelAndView mav = new ModelAndView("admin/employee/result");
		
		String str="";
		for(int i=0;i<delList.length-1;i++) {
			str += delList[i].toString() +",";
		}
		str += delList[delList.length-1].toString();
		
		try {
			List<String> idNoList = new ArrayList<String>();
			StringTokenizer checkListFilter = new StringTokenizer(str, ","); 

			while(checkListFilter.hasMoreTokens()) 
			{ 
				idNoList.add(checkListFilter.nextToken());
			}
			for(int i =0 ; i<idNoList.size(); i++) {
				System.out.println("넘어온 값 : " + idNoList.get(i));
			}

			empService.deleteMember(idNoList);
			mav.addObject("resultCode", "success");
		} catch (Exception e) {
			mav.addObject("resultCode", "fail");
		}
		return mav;
	}
	
	
	
	
	@RequestMapping(value = "admin/userDetailView.daou")
	public ModelAndView userDetailView(HttpServletRequest request, HttpServletResponse response, EmpVO evo) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/employee/userDetailView");
		
		HashMap<String,Object> memberViewAll = empService.memberViewAll(evo.getEmp_id());
		mav.addObject("memberViewAll", memberViewAll);
		return mav;
	}

	
	
	@RequestMapping(value = "admin/employeeUpdtForm.daou")
	public ModelAndView employeeUpdtForm(HttpServletRequest request, HttpServletResponse response, EmpVO evo) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("admin/employee/userUpdateForm");

		List<EmpVO> deptList = empService.deptList();
		List<EmpVO> gradeList = empService.gradeList();
		
		EmpVO memberView = empService.memberView(evo);
	
		mav.addObject("memberView", memberView);
		mav.addObject("deptList", deptList);
		mav.addObject("gradeList", gradeList);

		return mav;
	}

	
	
	@RequestMapping(value = "admin/employeeUpdt.daou")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, EmpVO evo) 
				throws Exception{
		ModelAndView mav = new ModelAndView("admin/employee/result");
			
		try {
			empService.updateMember(evo);
			mav.addObject("resultCode", "success");
			return mav;
		} catch (Exception e) {
			mav.addObject("resultCode", "error");
		}
		return mav;
	}
	
	

	@RequestMapping(value = "admin/employeeIdCheck.daou")
	public ModelAndView employeeIdCheck(HttpServletRequest request, HttpServletResponse response, EmpVO evo) {
		ModelAndView mav = new ModelAndView("admin/employee/result");

		try {
			int result = empService.employeeIdCheck(evo);
			if (result == checkKey) {
				mav.addObject("resultCode", "error");
			} else {
				mav.addObject("resultCode", "success");
			}
		} catch (Exception e) {
			mav.addObject("resultCode", "error");
		}
		return mav;
	}

}
