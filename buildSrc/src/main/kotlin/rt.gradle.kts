plugins {
    id("java")
    checkstyle
    idea
}

repositories {
    mavenCentral()
}

val mockkVersion = "1.11.0"

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation("io.github.microutils:kotlin-logging:1.12.5")

    implementation("org.springframework.kafka:spring-kafka:2.7.0")
    implementation("org.springframework.kafka:spring-kafka-test:2.7.0")
    implementation("commons-codec:commons-codec:1.15")

    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

}

