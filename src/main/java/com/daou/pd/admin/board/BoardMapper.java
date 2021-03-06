package com.daou.pd.admin.board;

import java.util.HashMap;
import java.util.List;
import com.daou.pd.user.exam.ExamVO;

public interface BoardMapper {
	
	public List<BoardVO> degreeList();
	
	public List<BoardVO> deptList();

	public List<BoardVO> gradeList();
	
	public int selectTarget(ExamVO exvo);
	
	public int selectTargetDo(ExamVO exvo);
	
	public int selectPass(ExamVO exvo);
	
	public double deptAverage(ExamVO exvo);
	
	public double gradeAverage(ExamVO exvo);
	
	public List<HashMap<String,Object>> rankWrongCount(ExamVO exvo);

}
