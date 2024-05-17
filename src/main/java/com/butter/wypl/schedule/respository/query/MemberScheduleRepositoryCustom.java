package com.butter.wypl.schedule.respository.query;

import java.time.LocalDate;
import java.util.List;

import com.butter.wypl.schedule.domain.MemberSchedule;

public interface MemberScheduleRepositoryCustom {

	List<MemberSchedule> findMemberSchedulesEndingTodayWithoutReview(int memberId, LocalDate today);

}
