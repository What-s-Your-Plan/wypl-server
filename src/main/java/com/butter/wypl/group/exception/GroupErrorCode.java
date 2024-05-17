package com.butter.wypl.group.exception;

import org.springframework.http.HttpStatus;

import com.butter.wypl.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GroupErrorCode implements ErrorCode {

	FAIL_TO_CREATE_GROUP(HttpStatus.BAD_REQUEST, "GROUP_001", "그룹 생성에 실패하였습니다."),
	EXCEED_MAX_MEMBER_COUNT(HttpStatus.BAD_REQUEST, "GROUP_002", "그룹에 속한 멤버는 최대 50명까지 가능합니다."),
	NOT_APPROPRIATE_TYPE_OF_GROUP_NAME(HttpStatus.BAD_REQUEST, "GROUP_003", "그룹 이름은 필수이며, 최대 20자까지 가능합니다."),
	EXCEED_MAX_LENGTH_OF_GROUP_DESCRIPTION(HttpStatus.BAD_REQUEST, "GROUP_004", "그룹 설명은 최대 50자까지 가능합니다."),
	NOT_EXIST_GROUP(HttpStatus.BAD_REQUEST, "GROUP_005", "존재하지 않는 그룹입니다."),
	IS_NOT_GROUP_MEMBER(HttpStatus.BAD_REQUEST, "GROUP_006", "그룹 멤버가 아닙니다."),
	HAS_NOT_DELETE_PERMISSION(HttpStatus.BAD_REQUEST, "GROUP_007", "그룹 삭제 권한이 없습니다."),
	NOT_EXIST_MEMBER_GROUP(HttpStatus.BAD_REQUEST, "GROUP_008", "해당 정보와 일치하는 그룹 맴버 정보가 없습니다."),
	NOT_EXIST_PENDING_MEMBER_GROUP(HttpStatus.BAD_REQUEST, "GROUP_009", "그룹 초대 대기 목록에 존재하지 않습니다."),
	NOT_ACCEPTED_LEAVE_GROUP(HttpStatus.BAD_REQUEST, "GROUP_010", "그룹을 나갈 수 없습니다."),
	HAS_NOT_INVITE_PERMISSION(HttpStatus.BAD_REQUEST, "GROUP_011", "그룹 초대 권한이 없습니다."),
	EXISTS_INVALID_MEMBER(HttpStatus.BAD_REQUEST, "GROUP_012", "유효하지 않은 회원이 존재합니다."),
	HAS_NOT_FORCE_OUT_PERMISSION(HttpStatus.BAD_REQUEST, "GROUP_013", "그룹 강퇴 권한이 없습니다."),
	;
	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	@Override
	public int getStatusCode() {
		return httpStatus.value();
	}
}
