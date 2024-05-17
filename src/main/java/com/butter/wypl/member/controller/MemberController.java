package com.butter.wypl.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Message;
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
import com.butter.wypl.member.repository.query.data.MemberSearchCond;
import com.butter.wypl.member.service.MemberLoadService;
import com.butter.wypl.member.service.MemberModifyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
	private final MemberLoadService memberLoadService;
	private final MemberModifyService memberModifyService;

	@GetMapping("/v1/members")
	public ResponseEntity<Message<MemberSearchResponse>> memberSearch(
			@Authenticated AuthMember authMember,
			MemberSearchCond cond
	) {
		MemberSearchResponse response = memberLoadService.searchMembers(authMember, cond);
		return ResponseEntity.ok(Message.withBody("사용자들을 조회했습니다", response));
	}

	@GetMapping("/v1/members/{member_id}")
	public ResponseEntity<Message<FindMemberProfileInfoResponse>> findProfileInfo(
			@Authenticated AuthMember authMember,
			@PathVariable("member_id") int memberId
	) {
		FindMemberProfileInfoResponse response = memberLoadService.findProfileInfo(authMember, memberId);
		return ResponseEntity.ok(Message.withBody("프로필 조회하였습니다.", response));
	}

	@GetMapping("/v1/timezones")
	public ResponseEntity<Message<FindTimezonesResponse>> findTimezones(
			@Authenticated AuthMember authMember
	) {
		FindTimezonesResponse response = memberLoadService.findAllTimezones(authMember);
		return ResponseEntity.ok(Message.withBody("타임존을 조회하였습니다.", response));
	}

	@PatchMapping("/v1/members/nickname")
	public ResponseEntity<Message<MemberNicknameUpdateResponse>> changeNickname(
			@Authenticated AuthMember authMember,
			@RequestBody MemberNicknameUpdateRequest request
	) {
		MemberNicknameUpdateResponse response = memberModifyService.updateNickname(authMember, request);
		return ResponseEntity.ok(Message.withBody("닉네임을 수정하였습니다.", response));
	}

	@PatchMapping("/v1/members/birthday")
	public ResponseEntity<Message<MemberBirthdayUpdateResponse>> changeBirthday(
			@Authenticated AuthMember authMember,
			@RequestBody MemberBirthdayUpdateRequest request
	) {
		MemberBirthdayUpdateResponse response = memberModifyService.updateBirthday(authMember, request);
		return ResponseEntity.ok(Message.withBody("생일을 수정하였습니다", response));
	}

	@PatchMapping("/v1/members/timezones")
	public ResponseEntity<Message<MemberTimezoneUpdateResponse>> changeTimezone(
			@Authenticated AuthMember authMember,
			@RequestBody MemberTimezoneUpdateRequest request
	) {
		MemberTimezoneUpdateResponse response = memberModifyService.updateTimezone(authMember, request);
		return ResponseEntity.ok(Message.withBody("타임존을 수정하였습니다.", response));
	}

	@PostMapping("/v1/members/profile-image")
	public ResponseEntity<Message<MemberProfileImageUpdateResponse>> changeProfileImage(
			@Authenticated AuthMember authMember,
			@RequestParam("image") MultipartFile image
	) {
		MemberProfileImageUpdateResponse response = memberModifyService.updateProfileImage(authMember, image);
		return ResponseEntity.ok(Message.withBody("프로필 이미지를 수정했습니다.", response));
	}

	@GetMapping("/v1/members/colors")
	public ResponseEntity<Message<MemberColorsResponse>> findColors(
			@Authenticated AuthMember authMember
	) {
		MemberColorsResponse response = memberLoadService.findColors(authMember);
		return ResponseEntity.ok(Message.withBody("컬러를 조회했습니다.", response));
	}

	@PatchMapping("/v1/members/colors")
	public ResponseEntity<Message<MemberColorUpdateResponse>> changeColors(
			@Authenticated AuthMember authMember,
			@RequestBody MemberColorUpdateRequest request
	) {
		MemberColorUpdateResponse response = memberModifyService.updateColor(authMember, request);
		return ResponseEntity.ok(Message.withBody("컬러를 수정하였습니다.", response));
	}
}
