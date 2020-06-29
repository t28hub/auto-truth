/*
 * Copyright 2020 Tatsuya Maki
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

plugins {
    java
    idea
}

repositories {
    mavenCentral()
}

dependencies {
    // AutoValue
    val autoValueVersion = "1.7.3"
    implementation("com.google.auto.value:auto-value-annotations:$autoValueVersion")
    annotationProcessor("com.google.auto.value:auto-value:$autoValueVersion")

    // Guava
    implementation("com.google.guava:guava:29.0-jre")

    // JSR305
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")

    // Truth
    val truthVersion = "1.0.1"
    testImplementation("com.google.truth:truth:$truthVersion")
    testImplementation("com.google.truth.extensions:truth-java8-extension:$truthVersion")

    // AutoTruth
    testImplementation(project(":annotations"))
    testAnnotationProcessor(project(":processor"))
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
