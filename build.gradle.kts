plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.epages.restdocs-api-spec") version "0.18.4"
    id("jacoco")
}

group = "com.butter"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

jacoco {
    toolVersion = "0.8.11"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    // 기본 logback 설정 제외 => log4j2 설정하기 위함
    all {
        exclude(module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenCentral()
}

val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
    /* Boot */
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    /* Log */
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    /* Database */
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    /* QueryDSL */
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    /* Lombok */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    /* Test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.mockito:mockito-core")
    runtimeOnly("com.h2database:h2")
    implementation("com.github.codemonstur:embedded-redis:1.4.3")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring31x:4.11.0")

    /* API Docs */
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.18.4")

    /* JWT */
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")

    /* S3 */
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    testImplementation("io.findify:s3mock_2.12:0.2.4")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

/* QueryDSL Start */
tasks.clean {
    doLast {
        file("src/main/generated").deleteRecursively()
    }
}
/* QueryDSL End */

/* Jacoco Start */
tasks.withType<JacocoReport> {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("reports/jacoco/index.xml"))
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/*Application*",
                    "**/Q*",
                    "**/*Cond*",
                    "**/*Request*",
                    "**/*Response*",
                    "**/annotation/**",
                    "**/cond/**",
                    "**/config/**",
                    "**/common/**",
                    "**/dto/**",
                    "**/data/**",
                    "**/exception/**",
                    "**/properties/**"
                )
            }
        })
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = true
            element = "CLASS"

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }

            excludes = listOf(
                "**/*Application*",
                "**/Q*",
                "**/*Cond*",
                "**/*Request*",
                "**/*Response*",
                "**/annotation/**",
                "**/cond/**",
                "**/config/**",
                "**/common/**",
                "**/dto/**",
                "**/data/**",
                "**/exception/**",
                "**/properties/**"
            )
        }
    }
}
/* Jacoco End */

/* API Docs Start */
openapi3 {
    this.setServer("http://127.0.0.1:8080")
    title = "What's Your Plan API Docs"
    description = "What's Your Plan API Description"
    version = "1.0.0"
    format = "yaml"
}

tasks.register<Copy>("copyOasToSwagger") {
    dependsOn(tasks.named("openapi3"))

    delete(file("src/main/resources/static/swagger-ui/openapi3.yaml"))
    from("build/api-spec/openapi3.yaml")
    into("src/main/resources/static/swagger-ui/.")

    doLast {
        val securitySchemesContent = """
          securitySchemes:
            APIKey:
              type: apiKey
              name: Authorization
              in: header
          security:
            - APIKey: [ ]  # Apply the security scheme here
        """
        file("src/main/resources/static/swagger-ui/openapi3.yaml").appendText(securitySchemesContent)
    }
}

tasks.build {
    dependsOn(tasks.named("copyOasToSwagger"))
}
/* API Docs End */
