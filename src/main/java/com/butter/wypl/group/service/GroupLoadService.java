package com.butter.wypl.group.service;

import com.butter.wypl.group.data.response.FindGroupMembersResponse;
import com.butter.wypl.group.data.response.FindGroupsResponse;

public interface GroupLoadService {

	FindGroupMembersResponse getDetailById(int memberId, int groupId);

	FindGroupsResponse getGroupsByMemberId(int memberId);
}
