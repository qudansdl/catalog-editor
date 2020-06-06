import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val isWindows = org.gradle.internal.os.OperatingSystem.current().name.startsWith("Windows")
val yarnCmd = if (isWindows) "yarn.cmd" else "yarn"

plugins {
	id("org.springframework.boot") version "2.2.7.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"

	id("com.google.cloud.tools.jib") version "2.1.0"
	id("pl.allegro.tech.build.axion-release") version "1.11.0"
	id("com.gorylenko.gradle-git-properties") version "2.2.2"


	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
	kotlin("kapt") version "1.3.72"
	idea
}

apply(plugin = "com.gorylenko.gradle-git-properties")
apply(plugin = "pl.allegro.tech.build.axion-release")


scmVersion {
	versionIncrementer ("incrementPatch")
	versionCreator ("versionWithBranch")

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

sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].resources {
	srcDirs("src/main/resources")

}

tasks {

	create<Exec>("yarnAdminInstall"){
		workingDir = file("../frontend/admin")
		commandLine = listOf(yarnCmd, "install")
	}
	create<Exec>("yarnAdminClean") {
		workingDir = file("../frontend/admin")
		commandLine = listOf(yarnCmd, "clean")
	}

	create<Exec>("yarnBuildAdminProd") {
		dependsOn("yarnAdminInstall")

		workingDir = file("../frontend/admin")
		commandLine = listOf(yarnCmd, "run", "build:prod")
	}

	create<Copy>("copyStaticAdmin") {
		dependsOn("yarnBuildAdminProd")
		from("../frontend/admin/dist") {
			include("**")
		}

		into("src/main/resources/public/admin")
	}



	create<Exec>("yarnEditorInstall"){
		workingDir = file("../frontend/editor")
		commandLine = listOf(yarnCmd, "install")
	}
	create<Exec>("yarnEditorClean") {
		workingDir = file("../frontend/editor")
		commandLine = listOf(yarnCmd, "clean")
	}


	create<Exec>("yarnBuildEditorProd") {
		dependsOn("yarnEditorInstall")

		workingDir = file("../frontend/editor")
		commandLine = listOf(yarnCmd, "run", "build:prod")
	}

	create<Copy>("copyStaticEditor") {
		dependsOn("yarnBuildEditorProd")
		from("../frontend/editor/dist/spa") {
			include("**")
		}

		into("src/main/resources/public/editor")
	}


	withType<BootJar> {
		dependsOn("copyStaticAdmin", "copyStaticEditor")
	}
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))

	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:7.0.1")
	implementation("com.graphql-java-kickstart:graphql-java-tools:6.0.2")
	implementation("com.graphql-java:graphql-java-extended-scalars:1.0")

	implementation("org.apache.tika:tika-core:1.24")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.webfolder:cdp4j:3.0.15")
	implementation("org.antlr:ST4:4.3")
	implementation("org.flywaydb:flyway-core")

	implementation("org.graalvm.sdk:graal-sdk:20.0.0")
	implementation("org.graalvm.js:js:20.0.0")
	implementation("org.graalvm.js:js-scriptengine:20.0.0")
	implementation("org.graalvm.tools:profiler:20.0.0")
	implementation("org.graalvm.tools:chromeinspector:20.0.0")


	implementation("com.querydsl:querydsl-jpa:4.2.2")
	implementation("com.querydsl:querydsl-core:4.2.2")
	kapt("com.querydsl:querydsl-apt:4.2.2:jpa")
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


jib {
	from {
		image = "openjdk:14-jdk-alpine"
		auth {
			username = System.getenv("DOCKER_USERNAME")
			password = System.getenv("DOCKER_PASSWORD")
		}
	}
	to {
		image = "catalog-editor-backend"
		tags = setOf(project.version.toString(), "latest")
		auth {
			username = System.getenv("DOCKER_USERNAME")
			password = System.getenv("DOCKER_PASSWORD")
		}
	}
	container {
		labels = mapOf(
				"maintainer" to "poh<qudansdl@gmail.com>"
		)
		jvmFlags = listOf("-XX:+ExitOnOutOfMemoryError", "-Dfile.encoding=UTF-8", "-Duser.timezone=UTC", "-Xms256m", "-Djava.awt.headless=true")
		mainClass = "com.basicit.GraphqlApplicationKt"
		ports = listOf("8081")
	}
}
