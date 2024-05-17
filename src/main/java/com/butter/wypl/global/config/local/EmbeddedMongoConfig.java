package com.butter.wypl.global.config.local;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;

@Profile({"default", "dev", "jenkins", "blue", "green"})
@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
@Configuration
public class EmbeddedMongoConfig {
}
