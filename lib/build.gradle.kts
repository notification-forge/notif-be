import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
    id("rt")
}

dependencies {
    implementation(project(":shared"))

    implementation("org.springframework.kafka:spring-kafka:2.7.0")
    implementation("org.springframework.kafka:spring-kafka-test:2.7.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}