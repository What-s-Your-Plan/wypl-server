package com.butter.wypl.group.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.butter.wypl.group.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
	@Query("SELECT g FROM Group g join fetch g.memberGroups WHERE g.id = :id")
	Optional<Group> findDetailById(int id);

	@Query("SELECT count(g) FROM Group g")
	int getGroupTotalCount();

}
