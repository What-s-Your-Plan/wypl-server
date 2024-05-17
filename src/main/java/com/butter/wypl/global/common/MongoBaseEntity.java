package com.butter.wypl.global.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MongoBaseEntity {

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime modifiedAt;

}
