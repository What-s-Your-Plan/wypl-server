package com.butter.wypl.schedule.respository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.respository.query.MemberScheduleRepositoryCustom;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Integer>,
	MemberScheduleRepositoryCustom {

	Optional<MemberSchedule> findByScheduleAndMember(Schedule schedule, Member member);

	List<MemberSchedule> findAllBySchedule(Schedule schedule);

	//해당 기간 내에 존재 하는 모든 일정
	@Query("select s "
		+ "from MemberSchedule ms join ms.schedule s "
		+ "where ms.member.id = :member_id "
		+ "and (:first_date between s.startDate and s.endDate "
		+ "or :last_date between s.startDate and s.endDate "
		+ "or s.startDate between :first_date and :last_date "
		+ "or s.endDate between :first_date and :last_date) "
		+ "order by s.startDate")
	List<Schedule> getCalendarSchedules(
		@Param("member_id") int memberId,
		@Param("first_date") LocalDateTime firstDate,
		@Param("last_date") LocalDateTime lastDate
	);

	//해당 기간 + 라벨에 해당 하는 모든 일정
	@Query("select s "
		+ "from MemberSchedule ms join ms.schedule s "
		+ "where ms.member.id = :member_id "
		+ "and (:first_date between s.startDate and s.endDate "
		+ "or :last_date between s.startDate and s.endDate "
		+ "or s.startDate between :first_date and :last_date "
		+ "or s.endDate between :first_date and :last_date) "
		+ "and s.label.labelId = :label_id "
		+ "order by s.startDate")
	List<Schedule> getCalendarSchedulesWithLabel(
		@Param("member_id") int memberId,
		@Param("first_date") LocalDateTime firstDate,
		@Param("last_date") LocalDateTime lastDate,
		@Param("label_id") int labelId
	);

	@Query("select m "
		+ "from MemberSchedule ms join ms.member m "
		+ "where ms.schedule.scheduleId = :schedule_id ")
	List<Member> getMemberWithSchedule(@Param("schedule_id") int scheduleId);

}
