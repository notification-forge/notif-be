plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm")
    application
    id("rt")
    idea
}

dependencies {
    implementation(project(":shared"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:7.1.0")
    implementation("com.graphql-java-kickstart:playground-spring-boot-starter:7.1.0")
    implementation("com.graphql-java-kickstart:voyager-spring-boot-starter:7.1.0")
    implementation("com.graphql-java:graphql-java-extended-scalars:1.0")
    implementation("com.graphql-java:graphql-java")

    implementation("org.hibernate.validator:hibernate-validator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:3.0.1")

    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

apply(plugin = "io.spring.dependency-management")