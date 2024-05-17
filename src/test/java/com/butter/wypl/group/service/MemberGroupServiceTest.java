package com.butter.wypl.group.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.group.repository.MemberGroupRepository;

@MockServiceTest
class MemberGroupServiceTest {

	@InjectMocks
	private MemberGroupService memberGroupService;

	@Mock
	private MemberGroupRepository memberGroupRepository;

}