plugins {
	java
	id("org.springframework.boot") version "3.5.15"
	id("io.spring.dependency-management") version "1.1.7"
	// id("org.asciidoctor.jvm.convert") version "4.0.3"
}

group = "com.dev_of_blue"
version = "1.0.0"
description = "bakery_product_service for products management"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
	mavenLocal()
}

// extra["snippetsDir"] = file("build/generated-snippets")
extra["springCloudVersion"] = "2025.0.3"

dependencies {
	implementation("org.devofblue:common-libs:1.0-SNAPSHOT")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	// runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// testImplementation("org.springframework.boot:spring-boot-testcontainers")
	// testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	// testImplementation("org.testcontainers:junit-jupiter")
	// testImplementation("org.testcontainers:postgresql")
    runtimeOnly("org.springframework.boot:spring-boot-docker-compose")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// tasks.test {
// 	outputs.dir(project.extra["snippetsDir"]!!)
// }
//
// tasks.asciidoctor {
// 	inputs.dir(project.extra["snippetsDir"]!!)
// 	dependsOn(tasks.test)
// }
