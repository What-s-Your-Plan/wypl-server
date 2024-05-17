package com.butter.wypl.global.common;

import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.GlobalErrorCode;
import com.butter.wypl.member.domain.Member;

public class BaseEntityTest {

	@Nested
	@DisplayName("BaseEntity 삭제 테스트")
	class DeleteTest {
		@DisplayName("삭제에 성공한다.")
		@Test
		void entityDeleteSuccess() {
			/* Given */
			Member member = KIM_JEONG_UK.toMember();

			/* When */
			member.delete();

			/* Then */
			assertThat(member.isDeleted()).isTrue();
		}

		@DisplayName("이미 삭제된 경우에 예외를 던진다.")
		@Test
		void alreadyDeletedEntityThrowException() {
			/* Given */
			Member member = KIM_JEONG_UK.toMember();
			member.delete();

			/* When */
			/* Then */
			assertThatThrownBy(member::delete)
					.isInstanceOf(CustomException.class)
					.hasMessageContaining(GlobalErrorCode.ALREADY_DELETED_ENTITY.getMessage());
		}
	}

	@Nested
	@DisplayName("BaseEntity 복구 테스트")
	class RestoreTest {
		@DisplayName("복구에 성공한다.")
		@Test
		void entityRestoreSuccess() {
			/* Given */
			Member member = KIM_JEONG_UK.toMember();
			member.delete();

			/* When */
			member.restore();

			/* Then */
			assertThat(member.isDeleted()).isFalse();
		}

		@DisplayName("삭제가 안 된 경우에 예외를 던진다.")
		@Test
		void alreadyRestoredEntityThrowException() {
			/* Given */
			Member member = KIM_JEONG_UK.toMember();

			/* When */
			/* Then */
			assertThatThrownBy(member::restore)
					.isInstanceOf(CustomException.class)
					.hasMessageContaining(GlobalErrorCode.NO_DELETED_ENTITY.getMessage());
		}
	}
}