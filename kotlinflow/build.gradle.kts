plugins {
    kotlin("jvm") version "2.0.20"
}

group = "styud.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0") // 최신 버전을 확인하여 사용

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    // JUnit 엔진
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}