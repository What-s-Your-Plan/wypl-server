package com.butter.wypl.notification.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.butter.wypl.global.common.MongoBaseEntity;
import com.butter.wypl.notification.data.NotificationTypeCode;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@Document(collection = "notifications")
@ToString
public class Notification extends MongoBaseEntity {

	@Id
	private String id;

	private int memberId; // 알림 받는 회원
	private String message; // 알림 메시지 => ex) "자율 A602"팀 그룹 초대가 왔어요.
	private boolean isRead; // 읽음 여부,
	private boolean isActed; // 회원 이벤트 처리여부
	private NotificationTypeCode typeCode;
	private int targetId; // NotificationTypeCode 에 따른 target Id => Group, Review

	/*
	* lombok 으로 getter 생성시 필드명에 is가 있으면 is를 포함시켜서 메소드를 생성
	* getter 메소드명 => isRead
	* Jackson 직렬화할때 key:value 형태로 만들기때문에 get or is를 뺀 read 로 생성
	* 이를 막기 위해 getter 메소드 직접 구현
	* */
	public boolean getIsRead() {
		return isRead;
	}
	public boolean getIsActed() {
		return isActed;
	}

	public void updateIsReadToTrue() {
		isRead = true;
	}

	public void updateIsActedToTrue() {
		isActed = true;
	}

}
