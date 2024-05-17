package com.butter.wypl.notification.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.butter.wypl.notification.domain.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
	// 최초 조회, _id 없는 경우
	@Query(value = "{memberId: ?0}", sort = "{_id: -1}")
	Page<Notification> findByMemberIdWithPage(int memberId, Pageable pageable);

	// 마지막행 ID 있는 경우
	@Query(value = "{memberId: ?0, _id: {$lt: {$oid: ?1} }}", sort = "{_id: -1}")
	Page<Notification> findAllByLastId(int memberId, String lastId, Pageable pageable);

	void deleteByMemberId(int memberId);

	List<Notification> findAllByMemberId(int memberId);

}
