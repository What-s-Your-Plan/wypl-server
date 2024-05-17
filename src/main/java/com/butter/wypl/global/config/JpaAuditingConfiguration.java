package com.butter.wypl.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfiguration {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
