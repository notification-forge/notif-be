import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

	kotlin("jvm")
    id("rt")
}

dependencies {
    implementation(project(":shared"))

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka:2.6.3")

    implementation("org.apache.kafka:kafka-streams:2.6.0")
    implementation("org.apache.kafka:kafka-clients:2.6.0")
    implementation("org.apache.kafka:connect-json:2.6.0")
    implementation("org.apache.kafka:kafka-streams-test-utils:2.6.0")
    implementation("org.apache.kafka:kafka_2.13:2.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

apply(plugin = "io.spring.dependency-management")