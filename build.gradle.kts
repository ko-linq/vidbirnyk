import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    `maven-publish`
}

group = "com.github.kotlinqs"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create("maven_public", MavenPublication::class) {
            groupId = "com.github.kotlinqs"
            artifactId = "vidbirnyk"
            version = "0.1-SNAPSHOT"

            from(components.getByName("java"))
        }
    }
}