package com.butter.wypl.group.domain;

import static com.butter.wypl.global.common.Color.*;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(MemberGroupId.class)
public class MemberGroup extends BaseEntity {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@Enumerated(EnumType.STRING)
	@Column(name = "color", length = 20, nullable = false)
	private Color color;

	@Enumerated(EnumType.STRING)
	@Column(name = "state", length = 20, nullable = false)
	private GroupInviteState groupInviteState;

	@Builder
	private MemberGroup(Member member, Group group, Color color, GroupInviteState groupInviteState) {
		this.member = member;
		this.group = group;
		this.color = color;
		this.groupInviteState = groupInviteState;
	}

	public static MemberGroup of(Member member, Group group) {
		return MemberGroup.builder()
			.member(member)
			.group(group)
			.color(labelYellow)
			.groupInviteState(GroupInviteState.PENDING)
			.build();
	}

	public static MemberGroup of(Member member, Group group, Color color) {
		return MemberGroup.builder()
			.member(member)
			.group(group)
			.color(color)
			.groupInviteState(GroupInviteState.PENDING)
			.build();
	}

	public static MemberGroup of(Member member, Group group, Color color, GroupInviteState groupInviteState) {
		return MemberGroup.builder()
			.member(member)
			.group(group)
			.color(color)
			.groupInviteState(groupInviteState)
			.build();
	}

	public void setGroupInviteStateAccepted() {
		this.groupInviteState = GroupInviteState.ACCEPTED;
	}

	/**
	 *  @hidden `JPA N + 1`문제가 발생할 수 있습니다.<p>
	 *  반드시 해당 함수를 사용하기 위해서는 `Fetch Join`을 사용하여 `Member`와 `Group`를 조회해야합니다.
	 */
	public boolean isOwner() {
		return this.group.getOwner().getId() == this.member.getId();
	}

	public void updateColor(Color color) {
		this.color = color;
	}
}
