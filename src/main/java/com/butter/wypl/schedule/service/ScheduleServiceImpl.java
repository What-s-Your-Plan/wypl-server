package com.butter.wypl.schedule.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.group.utils.MemberGroupServiceUtils;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.repository.LabelRepository;
import com.butter.wypl.label.utils.LabelServiceUtils;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.schedule.data.ModificationType;
import com.butter.wypl.schedule.data.request.ScheduleCreateRequest;
import com.butter.wypl.schedule.data.request.ScheduleUpdateRequest;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.butter.wypl.schedule.data.response.ScheduleDetailResponse;
import com.butter.wypl.schedule.data.response.ScheduleIdListResponse;
import com.butter.wypl.schedule.data.response.ScheduleIdResponse;
import com.butter.wypl.schedule.data.response.ScheduleResponse;
import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;
import com.butter.wypl.schedule.respository.ScheduleRepository;
import com.butter.wypl.schedule.utils.ScheduleServiceUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleModifyService, ScheduleReadService {

	private final ScheduleRepository scheduleRepository;
	private final LabelRepository labelRepository;
	private final MemberGroupRepository memberGroupRepository;

	private final MemberScheduleService memberScheduleService;
	private final RepetitionService repetitionService;

	@Override
	@Transactional
	public ScheduleDetailResponse createSchedule(int memberId, ScheduleCreateRequest scheduleCreateRequest) {
		Label label = scheduleCreateRequest.labelId() == null ? null
				: LabelServiceUtils.getLabelByLabelId(labelRepository, scheduleCreateRequest.labelId()); //라벨 유효성 검사

		Schedule schedule = scheduleRepository.save(scheduleCreateRequest.toEntity(label)); //반복이 없다는 가정하에 저장

		//그룹일 경우 멤버가 포함되었는지 확인
		List<MemberIdResponse> memberIdResponses = new ArrayList<>();

		if (schedule.getGroupId() != null) {
			memberIdResponses.addAll(
				MemberGroupServiceUtils.getAcceptedMembersOfGroup(memberGroupRepository, schedule.getGroupId())
					.stream()
					.map(member -> new MemberIdResponse(member.getId())).toList());
		} else {
			memberIdResponses.add(new MemberIdResponse(memberId));
		}

		//멤버-일정 테이블 업데이트
		List<Member> memberResponses = memberScheduleService.createMemberSchedule(schedule,
				memberIdResponses);

		//반복이 있을 경우 반복 일정 추가
		if (scheduleCreateRequest.repetition() != null) {
			Repetition repetition = repetitionService.createRepetition(scheduleCreateRequest.repetition().toEntity());

			schedule.updateRepetition(repetition);
			createRepetitionSchedules(schedule, repetition, memberId);
		}

		return ScheduleDetailResponse.of(schedule, memberResponses);
	}

	@Override
	@Transactional
	public ScheduleDetailResponse updateSchedule(int memberId, int scheduleId,
			ScheduleUpdateRequest scheduleUpdateRequest) {
		Schedule schedule = ScheduleServiceUtils.findById(scheduleRepository, scheduleId);

		//스케줄에 속한 멤버인지 확인(권한 확인)
		memberScheduleService.validateMemberSchedule(schedule, memberId);

		//라벨 update & 라벨 유효성 검사
		if (scheduleUpdateRequest.labelId() != null) {
			schedule.updateLabel(LabelServiceUtils.getLabelByLabelId(labelRepository, scheduleUpdateRequest.labelId()));
		} else {
			schedule.updateLabel(null);
		}

		//라벨, 속한 멤버, 반복 외의 일정 update
		schedule.update(scheduleUpdateRequest);

		//멤버-일정 update
		List<Member> members = memberScheduleService.updateMemberSchedule(schedule, scheduleUpdateRequest.members());

		//반복 일정이 바뀐 게 없으면 아래 과정 생략

		//이전 일정의 반복과 관련된 일정 삭제
		List<Schedule> modifySchedules = modifyRepetitionSchedule(schedule, scheduleUpdateRequest.modificationType());
		for (Schedule modifySchedule : modifySchedules) {
			memberScheduleService.getMemberScheduleByMemberAndSchedule(memberId, modifySchedule).delete();
			modifySchedule.delete();
		}

		if (scheduleUpdateRequest.modificationType().equals(ModificationType.ALL)) {
			repetitionService.deleteRepetition(schedule.getRepetition());
		}

		Repetition updatedRepetition =
				(scheduleUpdateRequest.repetition() == null) ? null :
						repetitionService.createRepetition(scheduleUpdateRequest.repetition().toEntity());
		schedule.updateRepetition(updatedRepetition);

		createRepetitionSchedules(schedule, updatedRepetition, memberId);

		return ScheduleDetailResponse.of(schedule, members);
	}

	@Override
	@Transactional
	public ScheduleIdListResponse deleteSchedule(int memberId, int scheduleId, ModificationType modificationType) {
		Schedule schedule = ScheduleServiceUtils.findById(scheduleRepository, scheduleId);

		//스케줄에 속한 멤버인지 확인(권한 확인)
		memberScheduleService.validateMemberSchedule(schedule, memberId);

		List<Schedule> deleteSchedules = modifyRepetitionSchedule(schedule, modificationType);
		deleteSchedules.add(schedule);

		//전체 삭제일 경우에는 관련 반복도 삭제
		if (modificationType.equals(ModificationType.ALL)) {
			repetitionService.deleteRepetition(schedule.getRepetition());
		}

		List<ScheduleIdResponse> scheduleIdResponses = new ArrayList<>();

		for (Schedule deleteSchedule : deleteSchedules) {
			memberScheduleService.getMemberScheduleByMemberAndSchedule(memberId, deleteSchedule).delete();
			deleteSchedule.delete();
			scheduleIdResponses.add(ScheduleIdResponse.from(deleteSchedule));
		}

		return ScheduleIdListResponse.from(scheduleIdResponses);
	}

	@Override
	public ScheduleDetailResponse getDetailScheduleByScheduleId(int memberId, int scheduleId) {
		Schedule schedule = ScheduleServiceUtils.findById(scheduleRepository, scheduleId);

		memberScheduleService.validateMemberSchedule(schedule, memberId);

		return ScheduleDetailResponse.of(schedule, memberScheduleService.getMembersBySchedule(schedule));
	}

	@Override
	public ScheduleResponse getScheduleByScheduleId(int memberId, int scheduleId) {
		Schedule schedule = ScheduleServiceUtils.findById(scheduleRepository, scheduleId);

		memberScheduleService.validateMemberSchedule(schedule, memberId);

		return ScheduleResponse.of(schedule, memberScheduleService.getMembersBySchedule(schedule));
	}

	private void createRepetitionSchedules(Schedule originSchedule, Repetition repetition, int memberId) {
		if (repetition == null) {
			return;
		}

		LocalDate repetitionStartDate = repetition.getRepetitionStartDate();
		LocalDate repetitionEndDate =
				repetition.getRepetitionEndDate() == null ? repetitionStartDate.plusYears(3) :
						repetition.getRepetitionEndDate();

		LocalDateTime startDateTime, endDateTime;
		startDateTime = originSchedule.getStartDate();
		endDateTime = originSchedule.getEndDate();

		switch (repetition.getRepetitionCycle()) {
			case YEAR -> {
				startDateTime = startDateTime.plusYears(1);
				endDateTime = endDateTime.plusYears(1);

				while (startDateTime.toLocalDate().isEqual(repetitionEndDate) || startDateTime.toLocalDate()
						.isBefore(repetitionEndDate)) {

					Schedule repetitionSchedule = scheduleRepository.save(
							originSchedule.toRepetitionSchedule(startDateTime, endDateTime));

					memberScheduleService.createMemberSchedule(repetitionSchedule,
							List.of(new MemberIdResponse(memberId)));

					startDateTime = startDateTime.plusYears(1);
					endDateTime = endDateTime.plusYears(1);
				}
			}
			case MONTH -> {
				startDateTime = startDateTime.plusMonths(1);
				endDateTime = endDateTime.plusMonths(1);

				while (startDateTime.toLocalDate().isEqual(repetitionEndDate) || startDateTime.toLocalDate()
						.isBefore(repetitionEndDate)) {

					Schedule repetitionSchedule = scheduleRepository.save(
							originSchedule.toRepetitionSchedule(startDateTime, endDateTime));

					memberScheduleService.createMemberSchedule(repetitionSchedule,
							List.of(new MemberIdResponse(memberId)));

					startDateTime = startDateTime.plusMonths(1);
					endDateTime = endDateTime.plusMonths(1);
				}
			}
			case WEEK -> {
				Duration diffDateTime = Duration.between(startDateTime, endDateTime);

				int repetitionWeek = originSchedule.getRepetition().getDayOfWeek();
				DayOfWeek dayOfWeek;

				int mask = 1;
				for (int i = 0; i < 7; i++) {
					if ((repetitionWeek & mask) != 0) {
						dayOfWeek = switch (i) {
							case 0 -> DayOfWeek.SUNDAY;
							case 1 -> DayOfWeek.MONDAY;
							case 2 -> DayOfWeek.TUESDAY;
							case 3 -> DayOfWeek.WEDNESDAY;
							case 4 -> DayOfWeek.THURSDAY;
							case 5 -> DayOfWeek.FRIDAY;
							case 6 -> DayOfWeek.SATURDAY;
							default -> throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_REPETITION_CYCLE);
						};

						startDateTime = originSchedule.getStartDate().with(TemporalAdjusters.next(dayOfWeek));
						endDateTime = startDateTime.plus(diffDateTime);

						while (startDateTime.toLocalDate().isEqual(repetitionEndDate) || startDateTime.toLocalDate()
								.isBefore(repetitionEndDate)) {

							Schedule repetitionSchedule = scheduleRepository.save(
									originSchedule.toRepetitionSchedule(startDateTime, endDateTime));

							memberScheduleService.createMemberSchedule(repetitionSchedule,
									List.of(new MemberIdResponse(memberId)));

							startDateTime = startDateTime.plusWeeks(1);
							endDateTime = endDateTime.plusWeeks(1);
						}
					}

					mask <<= 1;
				}
			}
			default -> throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_REPETITION_CYCLE);
		}

	}

	private List<Schedule> modifyRepetitionSchedule(Schedule originalSchedule,
			ModificationType modificationType) {
		List<Schedule> modifySchedules = new ArrayList<>();

		switch (modificationType) {
			case NOW -> {
			}
			case AFTER -> {
				modifySchedules.addAll(
						scheduleRepository.findAllByRepetitionAndStartDateAfter(originalSchedule.getRepetition(),
								originalSchedule.getStartDate()));
			}
			case ALL -> {
				modifySchedules.addAll(
						scheduleRepository.findAllByRepetitionAndStartDateBefore(originalSchedule.getRepetition(),
								originalSchedule.getStartDate()));
				modifySchedules.addAll(
						scheduleRepository.findAllByRepetitionAndStartDateAfter(originalSchedule.getRepetition(),
								originalSchedule.getStartDate()));
			}
			default -> throw new ScheduleException(ScheduleErrorCode.NOT_APPROPRIATE_MODIFICATION_TYPE);
		}

		return modifySchedules;
	}

}
