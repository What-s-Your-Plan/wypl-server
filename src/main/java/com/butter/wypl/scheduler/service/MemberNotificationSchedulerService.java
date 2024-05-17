package com.butter.wypl.scheduler.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.member.utils.MemberServiceUtils;
import com.butter.wypl.notification.service.ReviewNotificationService;
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;
import com.butter.wypl.schedule.utils.ScheduleServiceUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberNotificationSchedulerService {

	private final MemberRepository memberRepository;
	private final MemberScheduleRepository memberScheduleRepository;
	private final ReviewNotificationService reviewNotificationService;

	/* TODO :
		생각해 볼 문제:
		 - member 수가 너무 많다면?
		 - 만들어야할 알림 수가 너무 많다면?
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void runDailyReviewScheduler() {
		/* 모든 회원 목록 조회 */
		List<Member> allActiveMembers = MemberServiceUtils.findAllActiveMembers(memberRepository);

		/* 회원의 오늘 날짜에 회고해야 할 회원스케줄 조회 */
		LocalDate today = LocalDate.now();
		allActiveMembers.forEach(member -> {
			List<MemberSchedule> memberSchedules = ScheduleServiceUtils.findMemberSchedulesEndingTodayWithoutReview(
				memberScheduleRepository, member.getId(), today);

			memberSchedules.forEach(memberSchedule -> {
				/* 회고 알림 생성 */
				reviewNotificationService.createReviewNotification(
					member.getId(), member.getNickname(), memberSchedule.getSchedule().getTitle(),
					memberSchedule.getSchedule().getScheduleId()
				);
			});
		});
	}
}
