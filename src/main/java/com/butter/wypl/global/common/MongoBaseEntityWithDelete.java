package com.butter.wypl.global.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.GlobalErrorCode;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class MongoBaseEntityWithDelete {
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public void delete() {
		if (isDeleted()) {
			throw new CustomException(GlobalErrorCode.ALREADY_DELETED_ENTITY);
		}
		this.deletedAt = LocalDateTime.now();
	}

	public void restore() {
		if (isNotDeleted()) {
			throw new CustomException(GlobalErrorCode.NO_DELETED_ENTITY);
		}
		this.deletedAt = null;
	}

	public boolean isNotDeleted() {
		return deletedAt == null;
	}

	public boolean isDeleted() {
		return !isNotDeleted();
	}

}
