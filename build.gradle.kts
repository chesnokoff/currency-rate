plugins {
    id("org.springframework.boot") version "4.0.2" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false

    base
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withId("io.spring.dependency-management") {
        the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
            imports {
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.1")
            }
        }
    }

    plugins.withId("java") {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(25))
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}
