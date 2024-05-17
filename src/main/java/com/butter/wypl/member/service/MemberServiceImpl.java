package com.butter.wypl.member.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.file.S3ImageProvider;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.global.validator.RequestValidator;
import com.butter.wypl.member.data.MemberSearchInfo;
import com.butter.wypl.member.data.request.MemberBirthdayUpdateRequest;
import com.butter.wypl.member.data.request.MemberColorUpdateRequest;
import com.butter.wypl.member.data.request.MemberNicknameUpdateRequest;
import com.butter.wypl.member.data.request.MemberTimezoneUpdateRequest;
import com.butter.wypl.member.data.response.FindMemberProfileInfoResponse;
import com.butter.wypl.member.data.response.FindTimezonesResponse;
import com.butter.wypl.member.data.response.MemberBirthdayUpdateResponse;
import com.butter.wypl.member.data.response.MemberColorUpdateResponse;
import com.butter.wypl.member.data.response.MemberColorsResponse;
import com.butter.wypl.member.data.response.MemberNicknameUpdateResponse;
import com.butter.wypl.member.data.response.MemberProfileImageUpdateResponse;
import com.butter.wypl.member.data.response.MemberSearchResponse;
import com.butter.wypl.member.data.response.MemberTimezoneUpdateResponse;
import com.butter.wypl.member.domain.CalendarTimeZone;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.member.repository.query.data.MemberSearchCond;
import com.butter.wypl.member.utils.MemberServiceUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberModifyService, MemberLoadService {

	private final MemberRepository memberRepository;

	private final S3ImageProvider s3ImageProvider;

	@Override
	public FindTimezonesResponse findAllTimezones(final AuthMember authMember) {
		Member findMember = MemberServiceUtils.findById(memberRepository, authMember.getId());

		List<CalendarTimeZone> timeZones = CalendarTimeZone.getTimeZones();

		return FindTimezonesResponse.of(findMember.getTimeZone(), timeZones);
	}

	@Override
	public FindMemberProfileInfoResponse findProfileInfo(
			final AuthMember authMember,
			final int memberId
	) {
		validateOwnership(authMember, memberId);

		Member findMember = MemberServiceUtils.findById(memberRepository, memberId);

		return FindMemberProfileInfoResponse.from(findMember);
	}

	private void validateOwnership(AuthMember authMember, int memberId) {
		if (authMember.getId() != memberId) {
			throw new MemberException(MemberErrorCode.PERMISSION_DENIED);
		}
	}

	@Transactional
	@Override
	public MemberNicknameUpdateResponse updateNickname(
			final AuthMember authMember,
			final MemberNicknameUpdateRequest request
	) {
		Member findMember = MemberServiceUtils.findById(memberRepository, authMember.getId());

		findMember.changeNickname(request.nickname());

		return new MemberNicknameUpdateResponse(findMember.getNickname());
	}

	@Transactional
	@Override
	public MemberBirthdayUpdateResponse updateBirthday(
			final AuthMember authMember,
			final MemberBirthdayUpdateRequest request
	) {
		Member findMember = MemberServiceUtils.findById(memberRepository, authMember.getId());

		findMember.changeBirthday(request.birthday());

		return MemberBirthdayUpdateResponse.from(findMember.getBirthday());
	}

	@Transactional
	@Override
	public MemberTimezoneUpdateResponse updateTimezone(
			final AuthMember authMember,
			final MemberTimezoneUpdateRequest request
	) {
		Member findMember = MemberServiceUtils.findById(memberRepository, authMember.getId());

		findMember.changeTimezone(request.timeZone());

		return new MemberTimezoneUpdateResponse(findMember.getTimeZone());
	}

	@Transactional
	@Override
	public MemberProfileImageUpdateResponse updateProfileImage(
			final AuthMember authMember,
			final MultipartFile image
	) {
		Member findMember = MemberServiceUtils.findById(memberRepository, authMember.getId());

		String updateProfileImageUrl = s3ImageProvider.uploadImage(image);
		findMember.changeProfileImage(updateProfileImageUrl);

		return new MemberProfileImageUpdateResponse(findMember.getProfileImage());
	}

	@Transactional
	@Override
	public MemberColorUpdateResponse updateColor(
			final AuthMember authMember,
			final MemberColorUpdateRequest request
	) {
		Member findMember = MemberServiceUtils.findById(memberRepository, authMember.getId());

		findMember.changeColor(request.color());

		return MemberColorUpdateResponse.from(findMember.getColor());
	}

	@Override
	public MemberColorsResponse findColors(final AuthMember authMember) {
		Member findMember = MemberServiceUtils.findById(memberRepository, authMember.getId());

		List<Color> colors = Arrays.stream(Color.values()).toList();

		return MemberColorsResponse.of(findMember.getColor(), colors);
	}

	@Override
	public MemberSearchResponse searchMembers(
			final AuthMember authMember,
			final MemberSearchCond cond
	) {
		RequestValidator.validateRequestSize(cond.size());

		List<MemberSearchInfo> memberSearchInfos = memberRepository.findBySearchCond(cond)
				.stream()
				.map(MemberSearchInfo::from)
				.toList();

		return MemberSearchResponse.from(memberSearchInfos);
	}
}
