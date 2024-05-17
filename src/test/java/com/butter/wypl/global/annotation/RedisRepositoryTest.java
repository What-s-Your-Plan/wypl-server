package com.butter.wypl.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.global.config.JpaAuditingConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JpaAuditingConfiguration.class)
@SpringBootTest
@Transactional
@ActiveProfiles({"test"})
public @interface RedisRepositoryTest {
}
