plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm")
    kotlin("plugin.spring") version "1.5.0"
    application
    id("rt")
    idea
    jacoco
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":lib"))

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springframework.security:spring-security-ldap:5.4.6")
    implementation("org.springframework.security:spring-security-core:5.4.6")
    implementation("org.springframework.security:spring-security-crypto:5.4.6")
    implementation("org.springframework.security:spring-security-web:5.4.6")
    implementation("org.springframework.security:spring-security-config:5.4.6")

    implementation("io.jsonwebtoken:jjwt-api:0.11.1")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.1")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.1")

    implementation("com.graphql-java-kickstart:graphql-java-tools:11.0.0")
    implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:11.0.0")
    implementation("com.graphql-java-kickstart:playground-spring-boot-starter:11.0.0")
    implementation("com.graphql-java-kickstart:voyager-spring-boot-starter:11.0.0")
    implementation("com.graphql-java-kickstart:altair-spring-boot-starter:11.0.0")
    implementation("com.graphql-java:graphql-java-extended-scalars:1.0")

    implementation("io.pebbletemplates:pebble:3.1.4")

    implementation("org.hibernate.validator:hibernate-validator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.integration:spring-integration-test")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("com.squareup.okhttp3:okhttp:3.14.9")
    testImplementation("com.h2database:h2:1.4.194")

    testImplementation("io.cucumber:cucumber-java8:6.8.0")
    testImplementation("io.cucumber:cucumber-junit:6.8.0")
    testImplementation("io.cucumber:cucumber-spring:6.8.0")
    testImplementation("org.junit.platform:junit-platform-commons:1.5.2")

    testImplementation ("org.junit.platform:junit-platform-commons:1.5.2")

    testImplementation("org.jacoco:org.jacoco.agent:0.8.5:runtime")

    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.0")

    // Dev dependencies - Actual dependencies for kafka are resolved from the in-built plugins
    implementation("org.springframework.kafka:spring-kafka:2.7.0")
    implementation("org.springframework.kafka:spring-kafka-test:2.7.0")

}

tasks.compileJava {
    this.inputs.files(tasks.processResources)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

configurations.create("cucumberRuntime") {
    extendsFrom(configurations["testImplementation"])
}

tasks.create("cucumber") {
    dependsOn("assemble", "test", "compileTestKotlin")

    val jacocoAgent = sourceSets["test"].runtimeClasspath.files
        .first { f -> f.name == "org.jacoco.agent-0.8.5-runtime.jar" }.absoluteFile

    doLast {
        javaexec {
            main = "io.cucumber.core.cli.Main"
            jvmArgs(
                "-javaagent:$jacocoAgent=destfile=$buildDir/jacoco/test.exec,append=false",
                "-Dspring.profiles.active=integration-test",
                "-Dspring.config.location=src/test/resources/application-properties"
            )
            classpath = configurations["cucumberRuntime"] + sourceSets["test"].output + sourceSets["main"].output
            args(
                "--plugin", "html:build/reports/cucumber-reports.html",
                "--glue", "features", "src/test/resources"
            )
        }
    }
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.5"
    reportsDir = file("$buildDir/reports")
}

tasks.jacocoTestReport {
    dependsOn("cucumber")
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.destination = file("${buildDir}/jacocoHtml")
    }
}

tasks.check {
    dependsOn("jacocoTestReport")
}

apply(plugin = "io.spring.dependency-management")