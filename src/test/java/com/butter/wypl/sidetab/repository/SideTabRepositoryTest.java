package com.butter.wypl.sidetab.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.sidetab.domain.SideTab;

import jakarta.persistence.EntityManager;

@JpaRepositoryTest
class SideTabRepositoryTest {

	@Autowired
	private SideTabRepository sideTabRepository;

	@Autowired
	private EntityManager entityManager;

	@DisplayName("사이드탭 저장에 성공한다.")
	@Test
	void saveTest() {
		/* Given */
		SideTab sideTab = SideTab.from(MemberFixture.KIM_JEONG_UK.toMember());

		/* When */
		SideTab savedSideTab = sideTabRepository.save(sideTab);

		/* Then */
		assertThat(savedSideTab).isNotNull();
	}

	@DisplayName("사이드탭 조회에 성공한다.")
	@Test
	void findTest() {
		/* Given */
		SideTab sideTab = SideTab.from(MemberFixture.KIM_JEONG_UK.toMember());
		SideTab savedSideTab = sideTabRepository.save(sideTab);

		entityManager.flush();
		entityManager.clear();

		/* When */
		Optional<SideTab> optionalSideTab = sideTabRepository.findById(savedSideTab.getId());

		/* Then */
		assertThat(optionalSideTab).isPresent();
	}
}