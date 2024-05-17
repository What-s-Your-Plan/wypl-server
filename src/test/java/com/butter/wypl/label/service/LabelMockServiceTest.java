package com.butter.wypl.label.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.fixture.GroupFixture;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.label.data.response.AllLabelListResponse;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.fixture.LabelFixture;
import com.butter.wypl.label.repository.LabelRepository;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;

@MockServiceTest
public class LabelMockServiceTest {

	@InjectMocks
	private LabelServiceImpl labelService;

	@Mock
	private LabelRepository labelRepository;

	@Mock
	private MemberGroupRepository memberGroupRepository;

	@Test
	@DisplayName("멤버의 모든 라벨과 그룹을 조회")
	void getAllLabel() {
		// Given
		Label label = LabelFixture.STUDY_LABEL.toLabel();
		Member member = MemberFixture.JWA_SO_YEON.toMember();
		Group group = GroupFixture.GROUP_WORK.toGroup(member);
		MemberGroup memberGroup = MemberGroup.of(member, group);

		given(labelRepository.findByMemberId(anyInt()))
			.willReturn(List.of(label));

		given(memberGroupRepository.findAllWithMemberAndGroupByMemberId(anyInt()))
			.willReturn(List.of(memberGroup));

		// When
		AllLabelListResponse result = labelService.getAllLabelsByMemberId(1);

		// Then
		assertThat(result.labelCount()).isEqualTo(2);
	}
}
