package com.daou.pd.user.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daou.pd.admin.item.ItemVO;
import com.daou.pd.admin.item.OptionVO;

public interface ExamMapper {

	List<ExamListVO> getExamList(String id);

	ExamVO getIntro(HashMap<String, Object> map);

	ExamUserVO getSubIntro(HashMap<String, Object> map);

	Integer checkDegree(HashMap<String, Object> map);

	List<ItemVO> getObj(HashMap<String, Integer> map);

	List<ItemVO> getOX(HashMap<String, Integer> map);

	List<ItemVO> getShort(HashMap<String, Integer> map);

	List<OptionVO> getOptionList(int item_no);

	void makeTest(Map<String, Object> map);

	Integer getExamNo(String id);

	List<ItemVO> getExam(int exam_no);

	List<OptionVO> getExamOptions(HashMap<String, Object> map);

	int getTime(int exam_no);

	int markAnswer(List<MarkVO> list);

	void changeStatus(HashMap<String, Object> map);

	void grading(List<MarkVO> mlist);

	List<MarkVO> getAnswerSheet(int exam_no);

	List<ExamListVO> getRecordList(String id);

	List<ItemVO> getTestNote(int exam_no);

	List<MarkVO> getMark(List<MarkVO> mlist);
}
