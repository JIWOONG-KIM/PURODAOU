package com.daou.pd.user.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.daou.pd.admin.item.ItemVO;
import com.daou.pd.admin.item.OptionVO;

@Component("examService")
public class ExamServiceImpl implements ExamService {
	@Resource(name = "sqlSession")
	private SqlSession sqlSession;
	private ExamMapper examMapper;

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Bean
	public ExamMapper examMapper() {
		return sqlSession.getMapper(ExamMapper.class);
	}

	@Override
	public List<ExamListVO> getExamList(String id) {
		return examMapper().getExamList(id);
	}

	@Override
	public ExamVO getIntro(HashMap<String, Object> map) {
		examMapper = examMapper();
		ExamVO evo = examMapper.getIntro(map);
		evo.setUservo(examMapper.getSubIntro(map));
		System.out.println(evo.getUservo().getStatus_name());
		return evo;
	}

	@Override
	public int checkDegree(HashMap<String, Object> map) {
		Integer n = examMapper().checkDegree(map);
		if (n == null)
			return 0;
		else
			return n;
	}

	@Override
	public List<ItemVO> getItemList(HashMap<String, Integer> map) {
		examMapper = examMapper();
		List<ItemVO> list = examMapper.getOX(map);
		list.addAll(examMapper.getObj(map));
		list.addAll(examMapper.getShort(map));
		return list;
	}

	@Override
	public List<OptionVO> getOption(int item_no) {
		return examMapper().getOptionList(item_no);
	}

	@Override
	public void makeTest(List<ExamDetailVO> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dlist", list);
		examMapper().makeTest(map);
	}

	@Override
	public Integer getExamNo(String id) {
		return examMapper().getExamNo(id);
	}

	@Override
	public List<ItemVO> getExam(int exam_no) {
		return examMapper().getExam(exam_no);
	}

	@Override
	public List<OptionVO> getExamOptions(HashMap<String, Object> map) {
		return examMapper().getExamOptions(map);
	}

	@Override
	public int getTime(int exam_no) {
		return examMapper().getTime(exam_no);
	}

	@Override
	public int markAnswer(List<MarkVO> list) {
		examMapper().markAnswer(list);
		return 0;
	}

	@Override
	public void changeStatus(HashMap<String, Object> map) {
		examMapper().changeStatus(map);
	}

	@Override
	public void grading(List<MarkVO> mlist) {
		examMapper().grading(mlist);
	}

	@Override
	public List<MarkVO> getAnswerSheet(int exam_no) {
		return examMapper().getAnswerSheet(exam_no);
	}

	@Override
	public List<ExamListVO> getRecordList(String id) {
		return examMapper().getRecordList(id);
	}

	@Override
	public List<ItemVO> getTestNote(int exam_no) {
		return examMapper().getTestNote(exam_no);
	}

	@Override
	public List<MarkVO> getMark(List<MarkVO> mlist) {
		return examMapper().getMark(mlist);
	}

}
