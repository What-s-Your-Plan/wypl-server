package com.butter.wypl.schedule.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.fixture.LabelFixture;
import com.butter.wypl.label.repository.LabelRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@JpaRepositoryTest
public class MemberScheduleRepositoryTest {

	private final MemberRepository memberRepository;

	private final ScheduleRepository scheduleRepository;

	private final LabelRepository labelRepository;

	private Schedule schedule1, schedule2;
	private Member member1, member2;
	private Label label;

	@Autowired
	public MemberScheduleRepositoryTest(
		MemberRepository memberRepository, ScheduleRepository scheduleRepository, LabelRepository labelRepository) {
		this.memberRepository = memberRepository;
		this.scheduleRepository = scheduleRepository;
		this.labelRepository = labelRepository;
	}

	@BeforeEach
	void init() {
		member1 = memberRepository.save(MemberFixture.JWA_SO_YEON.toMemberWithId(1));
		member2 = memberRepository.save(MemberFixture.JO_DA_MIN.toMemberWithId(2));

		label = labelRepository.save(LabelFixture.STUDY_LABEL.toLabel());

		schedule1 = scheduleRepository.save(ScheduleFixture.PERSONAL_SCHEDULE.toScheduleWithLabel(1, label));
		schedule2 = scheduleRepository.save(ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toScheduleWithLabel(2, label));
	}

	@Test
	@DisplayName("멤버-회원 생성")
	void create() {

	}

	@Test
	@DisplayName("캘린더 일정 목록 조회")
	void getCalendarSchedules() {

	}

	@Test
	@DisplayName("라벨의 일정 조회")
	void getCalendarSchedulesWithLabel() {

	}

	@Test
	@DisplayName("스케줄에 해당하는 멤버만 조회")
	void getMembersBySchedule() {

	}
}
