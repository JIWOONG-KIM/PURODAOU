package com.daou.pd.user.exam;

import java.util.HashMap;
import java.util.List;

import com.daou.pd.admin.item.ItemVO;
import com.daou.pd.admin.item.OptionVO;

public interface ExamService {

	List<ExamListVO> getExamList(String id);

	ExamVO getIntro(HashMap<String, Object> map);

	int checkDegree(HashMap<String, Object> map);

	List<ItemVO> getItemList(HashMap<String, Integer> map);

	List<OptionVO> getOption(int item_no);

	void makeTest(List<ExamDetailVO> list);

	Integer getExamNo(String id);

	List<ItemVO> getExam(int exam_no);

	List<OptionVO> getExamOptions(HashMap<String, Object> map);

	int getTime(int exam_no);

	int markAnswer(HashMap<String, Object> map);

	void changeStatus(HashMap<String, Object> map);

	void grading(List<MarkVO> mlist);

	List<MarkVO> getAnswerSheet(int exam_no);

	List<ExamListVO> getRecordList(String id);

	List<ItemVO> getTestNote(int exam_no);
	
	List<String> getWrongNoteOptions(HashMap<String, String> map);

}
