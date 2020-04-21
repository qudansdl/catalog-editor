import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
	kotlin("kapt") version "1.3.72"
	idea
}

group = "com.basicit"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
}

sourceSets {
	kotlin.sourceSets.register("$buildDir/generated/source/kapt/main")
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))

	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:7.0.1")
	implementation("com.graphql-java-kickstart:graphql-java-tools:6.0.2")

	implementation("org.apache.tika:tika-core:1.24")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.webfolder:cdp4j:3.0.15")
	implementation("com.mashape.unirest:unirest-java:1.4.9")
	implementation("org.antlr:ST4:4.3")
	implementation("org.flywaydb:flyway-core")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("com.querydsl:querydsl-jpa:4.3.1")
	kapt("com.querydsl:querydsl-apt:4.3.1:jpa")
	kapt("javax.persistence:javax.persistence-api")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	runtimeOnly( "com.graphql-java-kickstart:altair-spring-boot-starter:7.0.1")
	runtimeOnly("com.graphql-java-kickstart:graphiql-spring-boot-starter:7.0.1")
	runtimeOnly("com.graphql-java-kickstart:voyager-spring-boot-starter:7.0.1")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

idea {
	module {
		val kaptMain = file("build/generated/source/kapt/main")
		sourceDirs.add(kaptMain)
		generatedSourceDirs.add(kaptMain)
	}
}
