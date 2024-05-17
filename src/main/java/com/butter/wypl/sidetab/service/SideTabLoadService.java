package com.butter.wypl.sidetab.service;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.sidetab.data.response.DDayWidgetResponse;
import com.butter.wypl.sidetab.data.response.GoalWidgetResponse;
import com.butter.wypl.sidetab.data.response.MemoWidgetResponse;

public interface SideTabLoadService {

	GoalWidgetResponse findGoal(final AuthMember authMember, final int goalId);

	DDayWidgetResponse findDDay(final AuthMember authMember, final int dDayId);

	MemoWidgetResponse findMemo(final AuthMember authMember, final int memoId);
}
