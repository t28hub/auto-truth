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

val kotlinVersion by extra { "1.3.72" }
val jsr305Version by extra { "3.0.2" }
val javaxAnnotationVersion by extra { "1.3.2" }
val truthVersion by extra { "1.0.1" }
val guavaVersion by extra { "29.0-jre" }
val autoValueVersion by extra { "1.7.3" }
val autoServiceVersion by extra { "1.1.1" }
val javapoetVersion by extra { "1.13.0" }
val junitVersion by extra { "5.6.2" }
val mockitoVersion by extra { "2.2.0" }
val compileTestingVersion by extra { "0.18" }
val kotlinCompileTestingVersion by extra { "1.2.9" }

subprojects {
    apply(plugin = "java")

    group = "io.t28.auto"
    version = "0.0.3-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = project.name
                    url = uri("https://maven.pkg.github.com/t28hub/auto-truth")
                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }
}
