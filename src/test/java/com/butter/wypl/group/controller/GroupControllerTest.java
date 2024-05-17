package com.butter.wypl.group.controller;

import static com.butter.wypl.global.common.Color.*;
import static com.butter.wypl.group.fixture.GroupFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.global.common.ControllerTest;
import com.butter.wypl.group.data.request.GroupCreateRequest;
import com.butter.wypl.group.data.request.GroupMemberColorUpdateRequest;
import com.butter.wypl.group.data.request.GroupMemberInviteRequest;
import com.butter.wypl.group.data.request.GroupUpdateRequest;
import com.butter.wypl.group.data.request.MemberIdRequest;
import com.butter.wypl.group.data.response.FindGroupMembersResponse;
import com.butter.wypl.group.data.response.FindGroupsResponse;
import com.butter.wypl.group.data.response.GroupIdResponse;
import com.butter.wypl.group.data.response.GroupMemberColorUpdateResponse;
import com.butter.wypl.group.data.response.GroupResponse;
import com.butter.wypl.group.data.response.MemberIdResponse;
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.repository.GroupRepository;
import com.butter.wypl.group.repository.MemberGroupRepository;
import com.butter.wypl.group.service.GroupLoadService;
import com.butter.wypl.group.service.GroupModifyService;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.MemberRepository;

class GroupControllerTest extends ControllerTest {

	@Autowired
	private GroupController groupController;

	@MockBean
	private GroupModifyService groupModifyService;

	@MockBean
	private GroupLoadService groupLoadService;

	@MockBean
	private GroupRepository groupRepository;

	@MockBean
	private MemberRepository memberRepository;

	@MockBean
	private MemberGroupRepository memberGroupRepository;

	@Test
	@DisplayName("그룹을 생성")
	void createGroupTest() throws Exception {

		/* Given */
		GroupCreateRequest createRequest = new GroupCreateRequest("group1", labelNavy,
				new HashSet<>(Arrays.asList(2, 3)));

		GroupResponse createResponse = new GroupResponse(1, "group1", labelNavy);
		given(groupModifyService.createGroup(anyInt(), any(GroupCreateRequest.class)))
				.willReturn(createResponse);

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.post("/group/v1/groups")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(convertToJson(createRequest))
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/create-group",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("name").type(JsonFieldType.STRING)
										.description("그룹 이름"),
								fieldWithPath("color").type(JsonFieldType.STRING)
										.description("그룹 메인 컬러"),
								fieldWithPath("member_id_list[]").type(JsonFieldType.ARRAY)
										.description("그룹 멤버 식별자 리스트")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.id").type(JsonFieldType.NUMBER)
										.description("그룹 식별자"),
								fieldWithPath("body.name").type(JsonFieldType.STRING)
										.description("그룹 이름"),
								fieldWithPath("body.color").type(JsonFieldType.STRING)
										.description("그룹 대표 색상")
						)
				))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("그룹 상세 정보를 조회")
	void getDetailTest() throws Exception {
		/* Given */
		int groupId = 1;
		// 회원 데이터 생성
		Member member1 = HAN_JI_WON.toMemberWithId(1);
		Member member2 = KIM_JEONG_UK.toMemberWithId(2);
		Member member3 = JWA_SO_YEON.toMemberWithId(3);
		member2.changeProfileImage("new.profile.image");

		// 그룹 데이터 생성
		Group group = GROUP_STUDY.toGroup(member1);
		MemberGroup member1Group = MemberGroup.of(member1, group, labelYellow);
		MemberGroup member2Group = MemberGroup.of(member2, group, labelYellow);
		MemberGroup member3Group = MemberGroup.of(member3, group, labelYellow);
		member3Group.setGroupInviteStateAccepted();

		// 회원 그룹 목록 연결
		group.getMemberGroups().add(member1Group);
		group.getMemberGroups().add(member2Group);
		group.getMemberGroups().add(member3Group);
		member1.getMemberGroups().add(member1Group);
		member2.getMemberGroups().add(member1Group);
		member3.getMemberGroups().add(member1Group);

		given(groupLoadService.getDetailById(anyInt(), anyInt()))
				.willReturn(FindGroupMembersResponse.of(Stream.of(member1Group, member2Group, member3Group)
						.map(FindGroupMembersResponse.FindGroupMember::from)
						.toList(), group.getColor()));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/group/v1/groups/{groupId}", groupId)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/get-detail-group",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.member_count").type(JsonFieldType.NUMBER)
										.description("조회한 그룹 회원수"),
								fieldWithPath("body.color").type(JsonFieldType.STRING)
										.description("회원의 그룹 컬러"),
								fieldWithPath("body.members[]").type(JsonFieldType.ARRAY)
										.description("조회한 그룹 회원 목록"),
								fieldWithPath("body.members[].id").type(JsonFieldType.NUMBER)
										.description("회원 식별자"),
								fieldWithPath("body.members[].email").type(JsonFieldType.STRING)
										.description("회원 이메일"),
								fieldWithPath("body.members[].nickname").type(JsonFieldType.STRING)
										.description("회원 닉네임"),
								fieldWithPath("body.members[].profile_image").type(JsonFieldType.STRING).optional()
										.description("회원 프로필 이미지"),
								fieldWithPath("body.members[].is_accepted").type(JsonFieldType.BOOLEAN)
										.description("가입 유무")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("그룹 수정")
	void updateGroupTest() throws Exception {

		/* Given */
		String name = "업데이트 할 그룹명";
		Color color = labelBrown;
		GroupUpdateRequest updateRequest = new GroupUpdateRequest(name, color);
		GroupResponse response = new GroupResponse(1, name, color);
		given(groupModifyService.updateGroup(anyInt(), anyInt(), any(GroupUpdateRequest.class)))
				.willReturn(response);
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/group/v1/groups/{groupId}", 1)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(convertToJson(updateRequest))
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/update-group",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						requestFields(
								fieldWithPath("name").type(JsonFieldType.STRING)
										.description("수정할 그룹 이름"),
								fieldWithPath("color").type(JsonFieldType.STRING)
										.description("수정할 그룹 색상")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.id").type(JsonFieldType.NUMBER)
										.description("그룹 식별자"),
								fieldWithPath("body.name").type(JsonFieldType.STRING)
										.description("그룹 이름"),
								fieldWithPath("body.color").type(JsonFieldType.STRING)
										.description("그룹 대표 색상")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("그룹 삭제")
	void deleteGroupTest() throws Exception {
		/* Given */
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/group/v1/groups/{groupId}", 1)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/delete-group",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원의 모든 그룹 목록을 조회")
	void findJoinGroupsTest() throws Exception {
		/* Given */
		// 회원 데이터 생성
		Member member = HAN_JI_WON.toMemberWithId(1);

		// 그룹 데이터 생성
		Group group1 = GROUP_STUDY.toGroup(member);
		MemberGroup member1Group1 = MemberGroup.of(member, group1, labelYellow);
		group1.getMemberGroups().add(member1Group1);
		Group group2 = GROUP_WORK.toGroup(member);
		MemberGroup member1Group2 = MemberGroup.of(member, group2, labelYellow);
		group2.getMemberGroups().add(member1Group2);

		// 회원 그룹 목록 연결
		member.getMemberGroups().add(member1Group1);
		member.getMemberGroups().add(member1Group2);

		List<FindGroupsResponse.FindGroup> groups = new ArrayList<>(
				List.of(FindGroupsResponse.FindGroup.from(member1Group1)));
		List<FindGroupsResponse.FindGroup> invitedGroups = new ArrayList<>(
				List.of(FindGroupsResponse.FindGroup.from(member1Group2)));
		FindGroupsResponse response = FindGroupsResponse.of(groups, invitedGroups);

		given(groupLoadService.getGroupsByMemberId(anyInt()))
				.willReturn(response);

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/group/v1/groups/members")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)

		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/get-groups-by-member-id",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.group_count").type(JsonFieldType.NUMBER)
										.description("가입한 그룹의 개수"),
								fieldWithPath("body.groups[]").type(JsonFieldType.ARRAY)
										.description("가입한 그룹"),
								fieldWithPath("body.groups[].id").type(JsonFieldType.NUMBER)
										.description("그룹 식별자"),
								fieldWithPath("body.groups[].name").type(JsonFieldType.STRING)
										.description("그룹 이름"),
								fieldWithPath("body.groups[].color").type(JsonFieldType.STRING)
										.description("그룹 라벨 색상"),
								fieldWithPath("body.groups[].is_owner").type(JsonFieldType.BOOLEAN)
										.description("그룹 주인 유무"),
								fieldWithPath("body.invited_group_count").type(JsonFieldType.NUMBER)
										.description("가입 대기중인 그룹의 개수"),
								fieldWithPath("body.invited_groups[]").type(JsonFieldType.ARRAY)
										.description("가입한 그룹"),
								fieldWithPath("body.invited_groups[].id").type(JsonFieldType.NUMBER)
										.description("그룹 식별자"),
								fieldWithPath("body.invited_groups[].name").type(JsonFieldType.STRING)
										.description("그룹 이름"),
								fieldWithPath("body.invited_groups[].color").type(JsonFieldType.STRING)
										.description("그룹 라벨 색상"),
								fieldWithPath("body.invited_groups[].is_owner").type(JsonFieldType.BOOLEAN)
										.description("그룹 주인 유무")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("그룹 회원 초대")
	void inviteGroupMemberTest() throws Exception {
		/* Given */
		Member member1 = KIM_JEONG_UK.toMemberWithId(2);
		Member member2 = JWA_SO_YEON.toMemberWithId(3);

		GroupMemberInviteRequest inviteRequest = new GroupMemberInviteRequest(Set.of(member1.getId(), member2.getId()));
		GroupIdResponse createResponse = new GroupIdResponse(1);

		given(groupModifyService.inviteGroupMember(anyInt(), anyInt(), any(GroupMemberInviteRequest.class)))
				.willReturn(createResponse);
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.post("/group/v1/groups/{groupId}/members/invitation", 1)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(convertToJson(inviteRequest))
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/invite-group-member",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						requestFields(
								fieldWithPath("member_id_list[]").type(JsonFieldType.ARRAY)
										.description("초대할 그룹 멤버 식별자 리스트")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.group_id").type(JsonFieldType.NUMBER)
										.description("그룹 식별자")
						)
				))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("그룹 회원 강제 퇴장")
	void forceOutGroupMemberTest() throws Exception {
		/* Given */
		int groupId = 1;
		Member member = KIM_JEONG_UK.toMemberWithId(2);
		MemberIdRequest memberIdRequest = new MemberIdRequest(member.getId());
		MemberIdResponse memberIdResponse = new MemberIdResponse(member.getId());

		BDDMockito.given(groupModifyService.forceOutGroupMember(
						anyInt(), anyInt(), any(MemberIdRequest.class)))
				.willReturn(memberIdResponse);

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/group/v1/groups/{groupId}/members/force-out", groupId)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(convertToJson(memberIdRequest))
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/group-member/force-out",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						requestFields(
								fieldWithPath("member_id").type(JsonFieldType.NUMBER)
										.description("강제 퇴장할 그룹 멤버 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.member_id").type(JsonFieldType.NUMBER)
										.description("강제 퇴장된 그룹 멤버 식별자")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("그룹 회원 초대 수락")
	void acceptGroupInvitationTest() throws Exception {
		/* Given */
		int groupId = 1;
		doNothing().when(groupModifyService).acceptGroupInvitation(anyInt(), anyInt());
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/group/v1/groups/{groupId}/members/invitation", groupId)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/accept-group-invitation",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("그룹 회원 초대 거절")
	void rejectGroupInvitationTest() throws Exception {
		/* Given */
		int groupId = 1;
		doNothing().when(groupModifyService).rejectGroupInvitation(anyInt(), anyInt());
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/group/v1/groups/{groupId}/members/invitation", groupId)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/accept-group-invitation",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("그룹 나가기")
	void leaveGroupTest() throws Exception {
		/* Given */
		int groupId = 1;
		doNothing().when(groupModifyService).leaveGroup(anyInt(), anyInt());
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/group/v1/groups/{groupId}/members", groupId)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/leave-group",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원 그룹 색상 변경")
	void updateGroupColorTest() throws Exception {
		/* Given */
		GroupMemberColorUpdateRequest request = new GroupMemberColorUpdateRequest(labelNavy);
		GroupMemberColorUpdateResponse response = new GroupMemberColorUpdateResponse(labelNavy);

		given(groupModifyService.updateGroupColor(anyInt(), anyInt(), any(GroupMemberColorUpdateRequest.class))
		).willReturn(response);
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/group/v1/groups/{groupId}/members/colors", 1)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(convertToJson(request))
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/member-group/update-color",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						requestFields(
								fieldWithPath("color").type(JsonFieldType.STRING)
										.description("변경할 그룹 색상")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.color").type(JsonFieldType.STRING)
										.description("변경된 그룹 색상")
						)
				))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원 그룹 색상 변경 실패")
	void updateGroupColorTestFail() throws Exception {
		/* Given */
		GroupMemberColorUpdateRequest request = new GroupMemberColorUpdateRequest(labelNavy);
		GroupMemberColorUpdateResponse response = new GroupMemberColorUpdateResponse(labelNavy);

		given(groupModifyService.updateGroupColor(anyInt(), anyInt(), any(GroupMemberColorUpdateRequest.class))
		).willReturn(response);
		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/group/v1/groups/{groupId}/members/colors", 1)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(convertToJson(request))
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("group/member-group/update-color",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("groupId").description("그룹 식별자")
						),
						requestFields(
								fieldWithPath("color").type(JsonFieldType.STRING)
										.description("변경할 그룹 색상")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.color").type(JsonFieldType.STRING)
										.description("변경된 그룹 색상")
						)
				))
				.andExpect(status().isOk());
	}

}