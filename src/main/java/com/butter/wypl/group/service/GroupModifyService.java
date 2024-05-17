package com.butter.wypl.group.service;

import com.butter.wypl.group.data.request.GroupCreateRequest;
import com.butter.wypl.group.data.request.GroupMemberColorUpdateRequest;
import com.butter.wypl.group.data.request.GroupMemberInviteRequest;
import com.butter.wypl.group.data.request.GroupUpdateRequest;
import com.butter.wypl.group.data.request.MemberIdRequest;
import com.butter.wypl.group.data.response.GroupIdResponse;
import com.butter.wypl.group.data.response.GroupMemberColorUpdateResponse;
import com.butter.wypl.group.data.response.GroupResponse;
import com.butter.wypl.group.data.response.MemberIdResponse;

public interface GroupModifyService {

	GroupResponse createGroup(int memberId, GroupCreateRequest createRequest);

	GroupResponse updateGroup(int memberId, int groupId, GroupUpdateRequest updateRequest);

	void deleteGroup(int memberId, int groupId);

	GroupIdResponse inviteGroupMember(int ownerId, int groupId, GroupMemberInviteRequest request);

	MemberIdResponse forceOutGroupMember(int ownerId, int groupId, MemberIdRequest request);

	void acceptGroupInvitation(int memberId, int groupId);

	void rejectGroupInvitation(int memberId, int groupId);

	void leaveGroup(int memberId, int groupId);

	GroupMemberColorUpdateResponse updateGroupColor(int id, int groupId, GroupMemberColorUpdateRequest request);
}
