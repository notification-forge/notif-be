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

    implementation("org.springframework.security:spring-security-ldap:5.4.1")
    implementation("org.springframework.security:spring-security-core:5.4.1")
    implementation("org.springframework.security:spring-security-crypto:5.4.1")
    implementation("org.springframework.security:spring-security-web:5.4.1")
    implementation("org.springframework.security:spring-security-config:5.4.1")

    implementation("io.jsonwebtoken:jjwt-api:0.11.1")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.1")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.1")

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