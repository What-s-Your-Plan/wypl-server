package com.butter.wypl.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.butter.wypl.global.config.JpaAuditingConfiguration;
import com.butter.wypl.global.config.MongoAuditingConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MongoAuditingConfiguration.class)
@DataMongoTest
@ActiveProfiles({"test"})
public @interface MongoRepositoryTest {
}
