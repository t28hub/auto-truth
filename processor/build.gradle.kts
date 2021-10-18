/*
 * Copyright 2019 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.internal.jvm.Jvm
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    jacoco
    `java-library`
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("io.gitlab.arturbosch.detekt") version "1.11.2"
    id("org.sonarqube") version "3.0"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // Truth
    val truthVersion: String by rootProject.extra
    implementation("com.google.truth:truth:$truthVersion")
    implementation("com.google.truth.extensions:truth-java8-extension:$truthVersion")
    implementation(project(":annotations"))

    // AutoService
    val autoServiceVersion: String by rootProject.extra
    implementation("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    kapt("com.google.auto.service:auto-service:$autoServiceVersion")

    // JavaPoet
    val javapoetVersion: String by rootProject.extra
    implementation("com.squareup:javapoet:$javapoetVersion")

    // Testing
    val junitVersion: String by rootProject.extra
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")

    val compileTestingVersion: String by rootProject.extra
    testImplementation("com.google.testing.compile:compile-testing:$compileTestingVersion")

    val kotlinCompileTestingVersion: String by rootProject.extra
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:$kotlinCompileTestingVersion")

    val mockitoVersion: String by rootProject.extra
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoVersion")

    testImplementation(files(Jvm.current().toolsJar))
}

tasks {
    val jvmTarget = "${JavaVersion.VERSION_1_8}"
    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    jacocoTestReport {
        reports {
            csv.isEnabled = false
            xml.isEnabled = true
            xml.destination = file("$buildDir/reports/jacoco/jacoco.xml")
            html.destination = file("$buildDir/reports/jacoco")
        }
        dependsOn("test")
    }
}

jacoco {
    toolVersion = "0.8.5"
}

ktlint {
    version.set("0.36.0")
    filter {
        include("src/main/kotlin/**")
        exclude("**/generated/**")
    }
    reporters {
        reporter(ReporterType.PLAIN)
    }
}

detekt {
    input = files("src/main/kotlin")
    config = files("${project.rootDir}/.detekt/config.yml")
    reports {
        html {
            enabled = true
            destination = file("$buildDir/reports/detekt/index.html")
        }
    }
}

val sonarAccessToken: String by project
sonarqube {
    properties {
        property("sonar.organization", "t28hub")
        property("sonar.host.url", "https://sonarcloud.io")

        property("sonar.projectKey", "io.t28.auto.truth")
        property("sonar.projectName", "auto-truth")
        property("sonar.jacoco.reportPaths", "build/jacoco/test.exec")
    }
}

publishing {
    publications {
        register<MavenPublication>("gpr") {
            group = project.group
            artifactId = "auto-truth-processor"
            version = "${project.version}"
            from(components["java"])
        }
    }
}
