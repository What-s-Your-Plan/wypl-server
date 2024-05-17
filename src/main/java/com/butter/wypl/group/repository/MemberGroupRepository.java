package com.butter.wypl.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.domain.MemberGroupId;
import com.butter.wypl.group.repository.query.MemberGroupRepositoryCustom;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, MemberGroupId>,
	MemberGroupRepositoryCustom {

}
