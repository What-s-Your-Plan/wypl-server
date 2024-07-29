package com.butter.wypl.schedule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.global.annotation.FacadeService;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.member.utils.MemberServiceUtils;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.exception.ScheduleErrorCode;
import com.butter.wypl.schedule.exception.ScheduleException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@FacadeService
public class MemberScheduleService {

	private final MemberRepository memberRepository;

	@Transactional
	public List<Member> createMemberSchedule(Schedule schedule, List<MemberIdResponse> memberIdResponses) {
		return null;
	}

	public void validateMemberSchedule(Schedule schedule, int memberId) {
	}

	@Transactional
	public List<Member> updateMemberSchedule(Schedule schedule, List<MemberIdResponse> memberIdResponses) {
//		List<Member> newMembers = new ArrayList<>();
//		List<Member> savedMembers = new ArrayList<>();
//		for (MemberIdResponse memberIdResponse : memberIdResponses) {
//			newMembers.add(MemberServiceUtils.findById(memberRepository, memberIdResponse.memberId()));
//		}

//		return savedMembers;
		return null;
	}

	public List<Member> getMembersBySchedule(Schedule schedule) {
//		List<Member> members = new ArrayList<>();
//		return members;
		return null;
	}

	
}
