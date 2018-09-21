package com.daou.pd.user.exam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daou.pd.admin.item.ItemVO;
import com.daou.pd.admin.item.OptionVO;

@Controller
public class ExamController {
	@Autowired
	@Resource(name = "examService")
	private ExamService examService;

	@RequestMapping(value = "user/exam/examlist.daou")
	public ModelAndView examlist(HttpServletRequest req) {
		String id = getSessionId(req);
		ModelAndView mav = new ModelAndView("user/exam/examMain");
		mav.addObject("elist", examService.getExamList(id));
		return mav;
	}

	@RequestMapping(value = "/user/exam/examIntro.daou")
	public ModelAndView examStart(@RequestParam("degree") String str, HttpServletRequest req) {
		String id = getSessionId(req);
		int degree = Integer.parseInt(str);
		ModelAndView mav = new ModelAndView("user/exam/examStart");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("reg_id", id);
		map.put("exam_degree", degree);
		mav.addObject("intro", examService.getIntro(map));

		return mav;
	}

	@RequestMapping(value = "/user/exam/getExam.daou")
	public ModelAndView examTest(@RequestParam("degree") String str, @RequestParam("ox_num") String ox,
			@RequestParam("obj_num") String obj, @RequestParam("short_num") String short_n,
			@RequestParam("category") String category, HttpServletRequest req, @RequestParam("examNo") String examNo,
			@RequestParam("categoryName") String categoryName, @RequestParam("exam_status") String exam_status) {// 시험여부
																													// 검사,
																													// 가져오기
		int degree = Integer.parseInt(str);
		int ox_num = Integer.parseInt(ox);
		int obj_num = Integer.parseInt(obj);
		int short_num = Integer.parseInt(short_n);
		int exam_category = Integer.parseInt(category);

		String id = getSessionId(req);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("degree", degree);
		map.put("id", id);

		int exam_no = Integer.parseInt(examNo);
		int left_time = examService.getTime(exam_no);
		if (examService.checkDegree(map) == 0) {
			makeExam(ox_num, obj_num, short_num, exam_category, exam_no);
		}
		List<ItemVO> itemList = examService.getExam(exam_no);
		map = new HashMap<String, Object>();
		List<OptionVO> olist = null;
		map.put("exam_no", exam_no);
		for (ItemVO item : itemList) {
			map.put("item_no", item.getItem_no());
			olist = examService.getExamOptions(map);
			item.setOvo(olist);
			map.remove("item_no");
		}

		System.out.println(exam_status);

		if (exam_status.equals("status02")) {
			System.out.println("답가져오기");
			map.put("mark", examService.getAnswerSheet(exam_no));
		}
		map.put("left_time", left_time);
		map.put("degree", degree);
		map.put("categoryName", categoryName);
		ModelAndView mav = new ModelAndView("user/exam/examTest");
		mav.addObject("info", map);
		mav.addObject("itemList", itemList);
		return mav;
	}

	private void makeExam(int ox_num, int obj_num, int short_num, int exam_category, int exam_no) {// 시험문제 랜덤 셀렉트
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("ox_num", ox_num);
		map.put("obj_num", obj_num);
		map.put("short_num", short_num);
		map.put("exam_category", exam_category);
		map.put("exam_no", exam_no);
		List<ItemVO> list = examService.getItemList(map);
		List<ExamDetailVO> dlist = new ArrayList<ExamDetailVO>();
		for (ItemVO item : list) {
			List<OptionVO> olist = examService.getOption(item.getItem_no());
//			List<OptionVO> olist2 = new ArrayList<OptionVO>();
			if (olist.size() > 1) {
				/*
				 * for (OptionVO op : olist) {//정답 주입 if (op.getCorrect_yn().equals("Y")) {
				 * olist2.add(op); break; } } for (OptionVO op : olist) {//오답 주입 if
				 * (op.getCorrect_yn().equals("N")) if (olist2.size() < 4) { olist2.add(op); } }
				 */
				Collections.shuffle(olist);
				item.setOvo(olist);
			} else {
				item.setOvo(olist);
			}
		}
		Collections.shuffle(list);
		for (ItemVO item : list) {
			ExamDetailVO detail = new ExamDetailVO();
			detail.setItem_no(item.getItem_no());
			detail.setExam_no(exam_no);
			List<OptionVO> ol = item.getOvo();
			if (item.getOvo().size() > 1) {
				for (OptionVO ov : ol) {
					if (ov.getCorrect_yn().equals("Y")) {
						detail.setExam_detail_correct(Integer.toString(ov.getOption_no()));
						break;
					}
				}
				detail.setExam_detail_option1(ol.get(0).getOption_no());
				detail.setExam_detail_option2(ol.get(1).getOption_no());
				detail.setExam_detail_option3(ol.get(2).getOption_no());
				detail.setExam_detail_option4(ol.get(3).getOption_no());
				dlist.add(detail);
			} else {
				detail.setExam_detail_option1(ol.get(0).getOption_no());
				detail.setExam_detail_correct(ol.get(0).getOption_contents());
				dlist.add(detail);
			}
		}
		examService.makeTest(dlist);
	}

	@RequestMapping(value = "/user/exam/regist.daou", produces = "application/text; charset=utf8") // 답안 제출
	@ResponseBody
	public String regist(HttpServletRequest req, @RequestBody List<MarkVO> list, @RequestParam("type") String type,
			@RequestParam("leftTime") String leftTime) {
		String id = getSessionId(req);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("alist", list);
		map.put("id", id);

		examService.markAnswer(list);
		String msg = null;
		map = new HashMap<String, Object>();
		map.put("id", id);
		int exam_no = list.get(0).getExam_no();
		map.put("exam_no", exam_no);

		if ("2".equals(type)) {// 임시저장
			map.put("exam_status", "status02");
			map.put("exam_left_time", leftTime);
			examService.changeStatus(map);
			msg = "임시주정 성공";
		} else if ("1".equals(type)) {// 최종제출
			map.put("exam_status", "status03");
			grading(exam_no);
			map.put("exam_left_time", 0);
			examService.changeStatus(map);
			msg = "제출 성공";
		}

		return msg;
	}

	private void grading(int exam_no) {// 채점
		List<MarkVO> list = examService.getAnswerSheet(exam_no);
		for (MarkVO m : list) {
			if (!m.getItem_type().equals("3")) {
				if (m.getExam_detail_correct().equals(m.getExam_detail_answer()))
					m.setCorrect_yn("Y");
				else
					m.setCorrect_yn("N");
			}
		}
		examService.grading(list);
	}

	private String getSessionId(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		String id = (String) session.getAttribute("user_emp_id");
		return id;
	}

	@RequestMapping(value = "/user/exam/recordList.daou")
	public ModelAndView getResultList(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView("user/exam/recordList");
		String id = getSessionId(req);
		List<ExamListVO> list = examService.getRecordList(id);
		mav.addObject("id", id);
		mav.addObject("rList", list);
		return mav;
	}

	@RequestMapping(value = "/user/exam/wrongAnswerNote.daou")
	public ModelAndView WrongAnswerNote(@RequestParam("examNo") String examNo) {
		ModelAndView mav = new ModelAndView("user/exam/wrongAnswerNote");
		List<MarkVO> mlist = examService.getAnswerSheet(Integer.parseInt(examNo));
//		for (MarkVO mark : mlist) {
//			if ("2".equals(mark.getItem_type())) {
//				List<String> strlist = examService.getMark(mlist);
//				if (strlist.size() > 1) {
//					mark.setExam_detail_answer(strlist.get(0));
//					mark.setExam_detail_correct(strlist.get(1));
//				} else {
//					mark.setExam_detail_correct(strlist.get(0));
//				}
//			}
//		}
		
		List<MarkVO> olist = examService.getMark(mlist);
		
		for (MarkVO m : mlist) {
			if("2".equals(m.getItem_type())) {
				
			}
		}
		List<ItemVO> ilist = examService.getTestNote(Integer.parseInt(examNo));
		mav.addObject("mlist", mlist);
		mav.addObject("ilist", ilist);
		return mav;
	}
}
