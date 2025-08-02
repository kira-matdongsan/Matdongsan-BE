import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    alias(libs.plugins.java.library)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    val libs = rootProject.libs

    apply(plugin = "java")
    apply(plugin = libs.plugins.java.library.get().pluginId)

    apply(plugin = libs.plugins.spring.boot.get().pluginId)
    apply(plugin = libs.plugins.spring.dependency.management.get().pluginId)

    configure<DependencyManagementExtension> {
        imports {
            mavenBom(libs.spring.cloud.dependencies.get().toString())
        }
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(libs.versions.javaLanguage.get().toInt())
        }
    }

    dependencies {
        compileOnly(libs.lombok)
        annotationProcessor(libs.lombok)

        testImplementation(libs.spring.boot.starter.test)
        testRuntimeOnly(libs.junit.platform.launcher)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

val libraryModules = listOf(":domain", ":core")
val appModules = listOf(":api", ":admin")

configure(libraryModules.map { project(it) }) {
    val jar: Jar by tasks
    jar.enabled = true
}

configure(appModules.map { project(it) }) {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks
    bootJar.enabled = true
    jar.enabled = true

    dependencies {
        implementation(project(":domain"))
        implementation(project(":core"))
    }
}