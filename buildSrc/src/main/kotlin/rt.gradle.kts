
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

    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

