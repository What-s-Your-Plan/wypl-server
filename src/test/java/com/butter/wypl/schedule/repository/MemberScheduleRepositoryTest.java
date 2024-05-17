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
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.respository.MemberScheduleRepository;
import com.butter.wypl.schedule.respository.ScheduleRepository;

@JpaRepositoryTest
public class MemberScheduleRepositoryTest {

	private final MemberScheduleRepository memberScheduleRepository;

	private final MemberRepository memberRepository;

	private final ScheduleRepository scheduleRepository;

	private final LabelRepository labelRepository;

	private Schedule schedule1, schedule2;
	private Member member1, member2;
	private Label label;

	@Autowired
	public MemberScheduleRepositoryTest(MemberScheduleRepository memberScheduleRepository,
		MemberRepository memberRepository, ScheduleRepository scheduleRepository, LabelRepository labelRepository) {
		this.memberScheduleRepository = memberScheduleRepository;
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
		// Given
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.schedule(schedule1)
			.member(member1)
			.build();

		// When
		MemberSchedule savedMemberSchedule = memberScheduleRepository.save(memberSchedule);

		// Then
		assertThat(savedMemberSchedule).isNotNull();
		assertThat(savedMemberSchedule.getMember()).isEqualTo(memberSchedule.getMember());
		assertThat(savedMemberSchedule.getSchedule()).isEqualTo(memberSchedule.getSchedule());
	}

	@Test
	@DisplayName("캘린더 일정 목록 조회")
	void getCalendarSchedules() {
		// Given
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.schedule(schedule1)
			.member(member1)
			.build();

		MemberSchedule memberSchedule2 = MemberSchedule.builder()
			.schedule(schedule2)
			.member(member1)
			.build();

		memberScheduleRepository.saveAll(
			List.of(memberSchedule, memberSchedule2)
		);

		// When
		List<Schedule> schedules = memberScheduleRepository.getCalendarSchedules(
			member1.getId(),
			LocalDateTime.of(2024, 4, 27, 0, 0),
			LocalDateTime.of(2024, 4, 27, 23, 59)
		);

		// Then
		assertThat(schedules.size()).isEqualTo(2);
		assertThat(schedules.get(0).getTitle()).contains(schedule2.getTitle(), schedule1.getTitle());
	}

	@Test
	@DisplayName("라벨의 일정 조회")
	void getCalendarSchedulesWithLabel() {
		// Given
		Schedule schedule3 = scheduleRepository.save(
			ScheduleFixture.LABEL_GROUP_SCHEDUEL.toScheduleWithLabel(3, label));

		MemberSchedule memberSchedule = MemberSchedule.builder()
			.schedule(schedule1)
			.member(member1)
			.build();

		MemberSchedule memberSchedule2 = MemberSchedule.builder()
			.schedule(schedule3)
			.member(member1)
			.build();

		memberScheduleRepository.saveAll(
			List.of(memberSchedule, memberSchedule2)
		);

		// When
		List<Schedule> schedules = memberScheduleRepository.getCalendarSchedulesWithLabel(
			member1.getId(),
			LocalDateTime.of(2024, 4, 27, 0, 0),
			LocalDateTime.of(2024, 4, 27, 23, 59),
			label.getLabelId()
		);

		// Then
		assertThat(schedules.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("스케줄에 해당하는 멤버만 조회")
	void getMembersBySchedule() {
		// Given
		MemberSchedule memberSchedule1 = MemberSchedule.builder()
			.member(member1)
			.schedule(schedule1)
			.build();

		MemberSchedule memberSchedule2 = MemberSchedule.builder()
			.member(member2)
			.schedule(schedule1)
			.build();

		memberScheduleRepository.saveAll(List.of(memberSchedule1, memberSchedule2));

		// When
		List<Member> members = memberScheduleRepository.getMemberWithSchedule(schedule1.getScheduleId());

		// Then
		assertThat(members.size()).isEqualTo(2);

	}
}
