package com.butter.wypl.group.fixture;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.member.domain.Member;

import lombok.Getter;

@Getter
public enum GroupFixture {

	GROUP_STUDY("study group", Color.labelBrown),
	GROUP_WORK("work group", Color.labelNavy);

	private final String name;
	private final Color color;
	private Member owner;

	GroupFixture(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public Group toGroup(Member owner) {
		return Group.of(name, color, owner);
	}
}
