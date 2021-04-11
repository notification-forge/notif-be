
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
//    implementation("com.graphql-java:graphql-java:11.0")
//    implementation("com.graphql-java:graphql-java-tools:5.2.4")
//    implementation("com.graphql-java:graphql-spring-boot-starter:5.0.2")
//    implementation("com.graphql-java:graphiql-spring-boot-starter:5.0.2")
//    implementation("com.graphql-java:voyager-spring-boot-starter:5.0.2")
//    implementation("com.expediagroup:graphql-kotlin-spring-server:1.4.2")
//    implementation("com.graphql-java:graphql-java-servlet:6.1.3")

    implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:7.1.0")
    implementation("com.graphql-java-kickstart:playground-spring-boot-starter:7.1.0")
    implementation("com.graphql-java-kickstart:voyager-spring-boot-starter:7.1.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

apply(plugin= "io.spring.dependency-management")