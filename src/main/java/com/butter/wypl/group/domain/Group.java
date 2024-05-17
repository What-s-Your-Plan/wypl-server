package com.butter.wypl.group.domain;

import static com.butter.wypl.group.exception.GroupErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "group_tbl")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private int id;

	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "color", length = 20)
	private Color color;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private Member owner;

	@OneToMany(mappedBy = "group")
	private List<MemberGroup> memberGroups;

	@Builder
	private Group(String name, Color color, Member owner) {
		this.name = name;
		this.color = color;
		this.owner = owner;
		this.memberGroups = new ArrayList<>();
	}

	public static Group of(String name, Color color, Member owner) {
		validateName(name);
		return Group.builder()
				.name(name)
				.color(color)
				.owner(owner)
				.build();
	}

	public static void validateName(String name) {
		if (name == null || name.isBlank() || name.length() > 20) {
			throw new GroupException(NOT_APPROPRIATE_TYPE_OF_GROUP_NAME);
		}
	}

	public void updateGroupInfo(String name, Color color) {
		validateName(name);
		this.name = name;
		this.color = color;
	}
}
