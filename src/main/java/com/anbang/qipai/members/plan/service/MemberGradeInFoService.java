package com.anbang.qipai.members.plan.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.plan.bean.MemberGrade;
import com.anbang.qipai.members.web.vo.GradeVo;

@Service
public class MemberGradeInFoService {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberGradeService memberGradeService;

	public GradeVo find_grade_info(String memberId) {
		GradeVo go = new GradeVo();
		MemberGrade memberGrade = memberGradeService.find_grade();
		MemberDbo memberDbo = memberAuthQueryService.findMemberById(memberId);
		double shortage = 0;
		double ProgressBar = 0;
		if (memberGrade != null && memberDbo != null) {
			if (memberDbo.getVipScore() >= 0 && memberDbo.getVipScore() < memberGrade.getVip1()) {
				shortage = memberGrade.getVip1() - memberDbo.getVipScore();// 距离下一等级相差多少
				ProgressBar = (double) memberDbo.getVipScore() / memberGrade.getVip1();// 进度条 ，前端显示
			} else if (memberDbo.getVipScore() >= memberGrade.getVip1()
					&& memberDbo.getVipScore() < memberGrade.getVip2()) {
				shortage = memberGrade.getVip2() - memberDbo.getVipScore();// 距离下一等级相差多少
				ProgressBar = (double) memberDbo.getVipScore() / memberGrade.getVip2();// 进度条 ，前端显示
			} else if (memberDbo.getVipScore() >= memberGrade.getVip2()
					&& memberDbo.getVipScore() < memberGrade.getVip3()) {
				shortage = memberGrade.getVip3() - memberDbo.getVipScore();// 距离下一等级相差多少
				ProgressBar = (double) memberDbo.getVipScore() / memberGrade.getVip3();// 进度条 ，前端显示
			} else if (memberDbo.getVipScore() >= memberGrade.getVip3()
					&& memberDbo.getVipScore() < memberGrade.getVip4()) {
				shortage = memberGrade.getVip4() - memberDbo.getVipScore();// 距离下一等级相差多少
				ProgressBar = (double) memberDbo.getVipScore() / memberGrade.getVip4();// 进度条 ，前端显示
			} else if (memberDbo.getVipScore() >= memberGrade.getVip4()
					&& memberDbo.getVipScore() < memberGrade.getVip5()) {
				shortage = memberGrade.getVip5() - memberDbo.getVipScore();// 距离下一等级相差多少
				ProgressBar = (double) memberDbo.getVipScore() / memberGrade.getVip5();// 进度条 ，前端显示
			} else if (memberDbo.getVipScore() >= memberGrade.getVip5()) {
				shortage = 0;
				ProgressBar = (double) 1;
			}
		}
		go.setShortage(shortage);
		go.setProgressBar(new BigDecimal(ProgressBar).setScale(2, BigDecimal.ROUND_HALF_UP));
		go.setData(memberDbo);
		return go;
	}
}
